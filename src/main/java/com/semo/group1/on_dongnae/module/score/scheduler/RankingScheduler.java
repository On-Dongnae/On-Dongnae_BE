package com.semo.group1.on_dongnae.module.score.scheduler;

import com.semo.group1.on_dongnae.module.score.cache.RankingCache;
import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;
import com.semo.group1.on_dongnae.module.score.repository.ScoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingScheduler {
    private final ScoreRepository scoreRepository;
    private final RankingCache rankingCache;

    private static final int USER_RANKING_LIMIT = 25;   // 개인 랭킹 가져올 개수
    private static final int REGION_RANKING_LIMIT = 25;  // 동네 랭킹 가져올 개수

    // 1.초기화 주기 설정 : 매월 1일 0시
    private LocalDateTime getStartOfMonth() {
        return YearMonth.now().atDay(1).atStartOfDay();
    }

    // 1-1. 다음달 초기화 날짜 데이터
    // ex) 이번달 랭킹은 _월 1일에 초기화됩니다.
    private LocalDateTime getNextMonth() {
        return getStartOfMonth().plusMonths(1);
    }

    // 2. 개인 랭킹 : 10분에 1번씩 업데이트
    @Scheduled(cron = "0 0/10 * * * *")
    public void updateUserRankingCache() { // 괄호 안 파라미터(int m) 제거 ✅
        // 상위 n명의 user들을 임시 저장(캐싱)
        List<UserRanking> rankings = scoreRepository.findUserRankings(
                getStartOfMonth(), getNextMonth(), PageRequest.of(0, USER_RANKING_LIMIT));

        // 캐싱해온 것을 바로 전달
        rankingCache.updateUserRankings(rankings);

        // 터미널에 진행 현황 메모 (남음 -> 갱신됨 으로 수정하면 더 자연스럽습니다)
        log.info("==> [10분 배치] 개인 랭킹 캐시 갱신 완료 ( {}건 갱신됨 )", rankings.size());
    }

    // 3. 동네 랭킹 : 매일 0시(자정)에 1번 업데이트
    @Scheduled(cron = "0 0 0 * * *")
    public void updateRegionRankingCache() { // 괄호 안 파라미터(int m) 제거 ✅
        // 상위 n개의 동네들을 임시 저장(캐싱)
        List<RegionRanking> rankings = scoreRepository.findRegionRankings(
                getStartOfMonth(), getNextMonth(), PageRequest.of(0, REGION_RANKING_LIMIT));

        // 캐싱해온 것을 바로 전달
        rankingCache.updateRegionRankings(rankings);

        // 터미널에 진행 현황 메모
        log.info("==> [1일 배치] 동네별 랭킹 캐시 갱신 완료 ( {}건 갱신됨 )", rankings.size());
    }
}