package com.example.barrier_free.domain.favorite;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.barrier_free.domain.favorite.dto.FavoritePlaceGroup;
import com.example.barrier_free.domain.favorite.dto.FavoritePlaceGroupResponse;
import com.example.barrier_free.domain.favorite.dto.FavoriteRequestDto;
import com.example.barrier_free.domain.favorite.dto.FavoriteResponse;
import com.example.barrier_free.domain.favorite.dto.PlaceRankResponse;
import com.example.barrier_free.domain.favorite.dto.YearWeek;
import com.example.barrier_free.domain.favorite.entity.Favorite;
import com.example.barrier_free.domain.favorite.entity.WeeklyRank;
import com.example.barrier_free.domain.favorite.repository.FavoriteRepository;
import com.example.barrier_free.domain.favorite.repository.WeeklyRankRepository;
import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.map.repository.MapRepository;
import com.example.barrier_free.domain.place.enums.PlaceType;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.report.repository.ReportRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.jwt.JwtUserUtils;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final MapRepository mapRepository;
	private final ReportRepository reportRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final WeeklyRankRepository weeklyRankRepository;

	@Transactional(readOnly = true)
	public List<PlaceRankResponse> getWeeklyTop3() {

		String redisKey = getLastWeekKey();
		YearWeek yearWeek = extractYearAndWeek(redisKey);

		List<WeeklyRank> rankings = weeklyRankRepository.findTop3ByYearAndWeekOrderByFavoriteCountDesc(
			yearWeek.getYear(), yearWeek.getWeek());

		if (rankings.isEmpty()) {
			// 지난 주 랭킹 없으면, 이번 주 데이터로 대체
			String currentKey = getCurrentKey();
			YearWeek currentYearWeek = extractYearAndWeek(currentKey);
			rankings = weeklyRankRepository.findTop3ByYearAndWeekOrderByFavoriteCountDesc(currentYearWeek.getYear(),
				currentYearWeek.getWeek());
		}

		return rankings.stream()
			.map(PlaceRankResponse::from)
			.collect((Collectors.toList()));
	}

	@Transactional
	public boolean toggleFavorite(FavoriteRequestDto request) {
		Long userId = JwtUserUtils.getCurrentUserId();
		Long placeId = request.getPlaceId();
		PlaceType type = request.getType();

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		String key = getCurrentKey(); // 현재 주간 키
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

	private String getCurrentKey() {
		LocalDate now = LocalDate.now();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		int weekNum = now.get(weekFields.weekOfWeekBasedYear());
		int year = now.getYear();
		return "popular:" + year + "-W" + weekNum;
	}

	private String getLastWeekKey() {
		LocalDate lastWeek = LocalDate.now().minusWeeks(1);
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		int week = lastWeek.get(weekFields.weekOfWeekBasedYear());
		int year = lastWeek.getYear();
		return "popular:" + year + "-W" + week;
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

	@Scheduled(cron = "0 0 3 ? * MON") // 매주 월요일일 새벽 3시 ->이전 좋아요 랭킹 redis에서 지우기
	public void cleanupLastWeeklyRankFromRedis() {
		String prevWeekKey = getLastWeekKey();
		redisTemplate.delete(prevWeekKey);
	}

	@Transactional
	@Scheduled(cron = "0 0 2 ? * MON") // 매주 월요일 새벽 2시
	public void saveWeeklyRanking() {
		//지난 주 기준 키를 부르기
		// popular: 2025 - W2  같은 형식의 키
		String redisKey = getLastWeekKey();

		//테스트 때문에 이미 있는 경우엔 db에서 지우고 스케쥴링함
		YearWeek yearWeek = extractYearAndWeek(redisKey);

		weeklyRankRepository.deleteByYearAndWeek(yearWeek.getYear(), yearWeek.getWeek());

		Set<ZSetOperations.TypedTuple<Object>> top3 = getTop3FromRedis(redisKey);

		if (top3 == null || top3.isEmpty())
			return;

		List<WeeklyRank> rankings = convertToWeeklyRanks(top3, redisKey);
		weeklyRankRepository.saveAll(rankings);
	}

	private Set<ZSetOperations.TypedTuple<Object>> getTop3FromRedis(String key) {
		return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 2);
	}

	private List<WeeklyRank> convertToWeeklyRanks(Set<ZSetOperations.TypedTuple<Object>> top3, String redisKey) {
		//year 랑 week 나누기
		YearWeek yearWeek = extractYearAndWeek(redisKey);

		List<WeeklyRank> result = new ArrayList<>();

		for (ZSetOperations.TypedTuple<Object> tuple : top3) {
			String redisValue = (String)tuple.getValue();
			Double score = tuple.getScore();

			if (redisValue == null || score == null)
				continue;

			parseWeeklyRank(redisValue, score.longValue(), yearWeek.getYear(), yearWeek.getWeek())
				.ifPresent(result::add);
		}

		return result;
	}

	private YearWeek extractYearAndWeek(String redisKey) {
		// redisKey - "popular:2025-W27"
		try {
			String[] split = redisKey.split(":")[1].split("-W");
			int year = Integer.parseInt(split[0]);
			int week = Integer.parseInt(split[1]);
			return new YearWeek(year, week);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.INVALID_REDISKEY);
		}
	}

	private Optional<WeeklyRank> parseWeeklyRank(String redisValue, long likeCount, int year, int week) {

		String[] parts = redisValue.split(":");
		if (parts.length != 2)
			return Optional.empty();

		String type = parts[0];
		Long id = Long.parseLong(parts[1]);

		return switch (type) {
			case "map" -> mapRepository.findById(id)
				.map(map -> WeeklyRank.from(map, year, week, likeCount));
			case "report" -> reportRepository.findById(id)
				.map(report -> WeeklyRank.from(report, year, week, likeCount));
			default -> Optional.empty();
		};

	}

	@Transactional
	public void saveCurrentWeeklyRanking() {
		String redisKey = getCurrentKey();
		YearWeek yearWeek = extractYearAndWeek(redisKey);
		weeklyRankRepository.deleteByYearAndWeek(yearWeek.getYear(), yearWeek.getWeek());

		Set<ZSetOperations.TypedTuple<Object>> top3 = getTop3FromRedis(redisKey);

		if (top3 == null || top3.isEmpty())
			return;

		List<WeeklyRank> rankings = convertToWeeklyRanks(top3, redisKey);
		weeklyRankRepository.saveAll(rankings);
	}

	@Transactional
	public FavoritePlaceGroupResponse getFavorite(List<Integer> facilities) {
		Long userId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		FavoritePlaceGroup favoritePlace = favoriteRepository.findFilteredFavorites(userId, facilities);

		List<FavoriteResponse> mapResponses = favoritePlace.getMapFavorites().stream()
			.map(FavoriteResponse::fromPlace)
			.toList();

		List<FavoriteResponse> reportResponses = favoritePlace.getReportFavorites().stream()
			.map(FavoriteResponse::fromPlace)
			.toList();

		return new FavoritePlaceGroupResponse(mapResponses, reportResponses);
	}
}


