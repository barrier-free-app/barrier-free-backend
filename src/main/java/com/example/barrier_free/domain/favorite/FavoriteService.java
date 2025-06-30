package com.example.barrier_free.domain.favorite;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.barrier_free.domain.favorite.dto.FavoriteRequestDto;
import com.example.barrier_free.domain.favorite.dto.PlaceResponse;
import com.example.barrier_free.domain.favorite.entity.Favorite;
import com.example.barrier_free.domain.favorite.entity.MonthlyRank;
import com.example.barrier_free.domain.favorite.repository.FavoriteRepository;
import com.example.barrier_free.domain.favorite.repository.MonthlyRankRepository;
import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.map.repository.MapRepository;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.report.repository.ReportRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final MapRepository mapRepository;
	private final ReportRepository reportRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final MonthlyRankRepository monthlyRankRepository;

	@Transactional(readOnly = true)
	public List<PlaceResponse> getMonthlyTop3() {
		YearMonth beforeMonth = YearMonth.now().minusMonths(1);
		String beforeMonthString = beforeMonth.toString();
		List<MonthlyRank> rankings = monthlyRankRepository.findByRankMonthOrderByFavoriteCountDesc(beforeMonthString);
		return rankings.stream()
			.map(PlaceResponse::from)
			.collect((Collectors.toList()));
	}

	@Transactional
	public boolean toggleFavorite(FavoriteRequestDto request) {
		Long userId = request.getUserId();
		Long placeId = request.getPlaceId();
		PlaceType type = request.getType();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		String key = getCurrentMonthKey();
		String redisValue = getRedisValue(type, placeId);

		Optional<Favorite> existingFavorite = findExistingFavorite(userId, placeId, type);

		if (existingFavorite.isPresent()) {
			favoriteRepository.delete(existingFavorite.get());
			redisTemplate.opsForZSet().incrementScore(key, redisValue, -1);
			Double score = redisTemplate.opsForZSet().score(key, redisValue);
			if (score != null && score <= 0) {
				redisTemplate.opsForZSet().remove(key, redisValue);
			}
			return false;
		}

		Favorite favorite = createFavorite(user, placeId, type);
		favoriteRepository.save(favorite);
		redisTemplate.opsForZSet().incrementScore(key, redisValue, 1);
		return true; // 좋아요 등록
	}

	private Favorite createFavorite(User user, Long placeId, PlaceType type) {
		if (type == PlaceType.map) {
			Map map = mapRepository.findById(placeId)
				.orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
			return Favorite.fromMap(user, map);
		} else {
			Report report = reportRepository.findById(placeId)
				.orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));

			return Favorite.fromReport(user, report);
		}

	}

	private String getCurrentMonthKey() {
		return "popular:" + YearMonth.now();
	}

	private String getRedisValue(PlaceType type, Long placeId) {
		return type.name().toLowerCase() + ":" + placeId;
	}

	private Optional<Favorite> findExistingFavorite(Long userId, Long placeId, PlaceType type) {
		Optional<Favorite> favorite;
		if (type == PlaceType.map) {
			favorite = favoriteRepository.findByUserIdAndMapId(userId, placeId);
		} else {
			favorite = favoriteRepository.findByUserIdAndReportId(userId, placeId);
		}
		return favorite;
	}

	@Scheduled(cron = "0 0 3 1 * ?") // 매월 1일 새벽 3시 ->이전 좋아요 랭킹 redis에서 지우기
	public void cleanupOldMonthlyRankings() {
		String prevMonthKey = "popular:" + YearMonth.now().minusMonths(1);
		redisTemplate.delete(prevMonthKey);
	}

	@Transactional
	@Scheduled(cron = "0 0 2 1 * ?") // 매월 1일 새벽 2시
	public void saveMonthlyRanking() {
		//지난 달 좋아요 총합에 따라 탑3를 DB에 저장
		YearMonth month = YearMonth.now().minusMonths(1);
		String redisKey = buildRedisKey(month);

		Set<ZSetOperations.TypedTuple<Object>> top3 = getTop3FromRedis(redisKey);

		if (top3 == null || top3.isEmpty())
			return;

		List<MonthlyRank> rankings = convertToMonthlyRanks(top3, month);
		monthlyRankRepository.saveAll(rankings);
	}

	private String buildRedisKey(YearMonth month) {
		return "popular:" + month;
	}

	private Set<ZSetOperations.TypedTuple<Object>> getTop3FromRedis(String key) {
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 2);
	}

	private List<MonthlyRank> convertToMonthlyRanks(Set<ZSetOperations.TypedTuple<Object>> top3, YearMonth month) {
		List<MonthlyRank> result = new ArrayList<>();

		for (ZSetOperations.TypedTuple<Object> tuple : top3) {
			String redisValue = (String)tuple.getValue();
			Double score = tuple.getScore();

			if (redisValue == null || score == null)
				continue;

			parseMonthlyRank(redisValue, score.longValue(), month)
				.ifPresent(result::add);
		}

		return result;
	}

	private Optional<MonthlyRank> parseMonthlyRank(String redisValue, long likeCount, YearMonth month) {
		String[] parts = redisValue.split(":");
		if (parts.length != 2)
			return Optional.empty();

		String type = parts[0];
		Long id = Long.parseLong(parts[1]);

		return switch (type) {
			case "map" -> mapRepository.findById(id)
				.map(map -> MonthlyRank.from(map, month, likeCount));
			case "report" -> reportRepository.findById(id)
				.map(report -> MonthlyRank.from(report, month, likeCount));
			default -> Optional.empty();
		};

	}
}


