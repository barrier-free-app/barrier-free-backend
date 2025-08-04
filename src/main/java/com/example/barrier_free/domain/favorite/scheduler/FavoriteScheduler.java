package com.example.barrier_free.domain.favorite.scheduler;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.barrier_free.domain.favorite.FavoriteService;
import com.example.barrier_free.domain.favorite.dto.YearWeek;
import com.example.barrier_free.domain.favorite.entity.WeeklyRank;
import com.example.barrier_free.domain.favorite.repository.WeeklyRankRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FavoriteScheduler {

	private final FavoriteService favoriteService;
	private final WeeklyRankRepository weeklyRankRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	@Scheduled(cron = "0 50 23 ? * MON")
	@Transactional
	public void saveWeeklyRanking() {
		String redisKey = favoriteService.getLastWeekKey();
		YearWeek yearWeek = favoriteService.extractYearAndWeek(redisKey);

		Set<ZSetOperations.TypedTuple<Object>> top3 = favoriteService.getTop3FromRedis(redisKey);
		log.info("[SCHEDULER] 주간 랭킹 저장 시작 - RedisKey: {}", redisKey);

		if (top3 == null || top3.isEmpty()) {
			log.warn("[SCHEDULER] Redis에 지난주 데이터 없음 - DB 삭제하지 않음");
			return;
		}

		weeklyRankRepository.deleteByYearAndWeek(yearWeek.getYear(), yearWeek.getWeek());

		List<WeeklyRank> rankings = favoriteService.convertToWeeklyRanks(top3, redisKey);
		log.info("[SCHEDULER] 저장할 랭킹 수: {}", rankings.size());

		weeklyRankRepository.saveAll(rankings);
		log.info("[SCHEDULER] 주간 랭킹 저장 완료");
	}

	@Scheduled(cron = "0 1 0 ? * TUE")
	public void cleanupLastWeeklyRankFromRedis() {
		String redisKey = favoriteService.getLastWeekKey();
		redisTemplate.delete(redisKey);
		log.info("[SCHEDULER] 지난주 Redis 키 삭제 완료 - {}", redisKey);
	}
}
