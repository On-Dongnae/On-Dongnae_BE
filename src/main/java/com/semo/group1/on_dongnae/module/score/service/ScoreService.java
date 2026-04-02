package com.semo.group1.on_dongnae.module.score.service;

import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.region.repository.RegionRepository;
import com.semo.group1.on_dongnae.module.score.cache.RankingCache;
import com.semo.group1.on_dongnae.module.score.dto.ScoreHistoryDto;
import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;
import com.semo.group1.on_dongnae.entity.Region;
import com.semo.group1.on_dongnae.entity.Score;
import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.entity.enums.ScoreType;
import com.semo.group1.on_dongnae.module.score.repository.ScoreRepository;

import com.semo.group1.on_dongnae.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final RankingCache rankingCache;

    @Transactional
    // 미션 완료(MISSION_COMPLETE) 확인 후 addMissionScore 메소드를 호출
    public void addMissionScore(Long userId, Long regionId, Integer amount, Long missionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_MISSION_NOT_FOUND));
        user.addScore(amount);
        //        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // getReferenceById(userId); => 그냥 점수만 추가 BUT 예외 처리 불가능

        /*Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("지역을 찾을 수 없습니다."));
        // getReferenceById(regionId); => 그냥 점수만 추가 BUT 예외 처리 불가능 */
        Score score = Score.builder()
                .user(user)
                .region(user.getRegion()) // call 간편화
                .amount(amount)
                .type(ScoreType.MISSION_COMPLETE)
                .referenceId(missionId) // ID 기록
                // 점수 누적 시간 추가
                .createdAt(LocalDateTime.now())
                .build();

        scoreRepository.save(score);

    }
    @Transactional
    // 관리자 요청(ADMIN_ADJUST) 후 adjustScoreMyAdmin 메소드를 호출 ex) 강제 개입
    public void adjustScoreByAdmin(Long userId, Long regionId, Integer amount, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // getReferenceById(userId); => 그냥 점수만 추가 BUT 예외 처리 불가능
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("지역을 찾을 수 없습니다."));
        // getReferenceById(regionId); => 그냥 점수만 추가 BUT 예외 처리 불가능
        Score score = Score.builder()
                .user(user)
                .region(region)
                .amount(amount)
                .type(ScoreType.ADMIN_ADJUST)
                .referenceId(adminId) // ID 기록
                .build();

        scoreRepository.save(score);
    }

    // 누적 점수 상위 n명의 user in 모든 지역
    public List<UserRanking>getTopNUserRanking(int n) {
        return rankingCache.getUserRankings(n);
    }

    // 누적 점수 상위 n개의 region
    public List<RegionRanking>getTopNRegionRanking(int n) {
        return rankingCache.getRegionRankings(n);
    }

    // 자신의 등수 데이터
    // ex) 내 등수 : 123등(n 초과)
    public Long getMyRanking(Long userId) {
        LocalDateTime start = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1);

        Long rank = scoreRepository.findMyRank(userId, start, end);
        // 미션 수행을 하나도 안 한 경우 => -1 return (Long type)
        return rank == null ? -1L : rank;


    }
    // 자신의 점수 데이터
    // ex) 내 점수 : 123온도
    public List<ScoreHistoryDto>getMyScoreHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Score> scores = scoreRepository.findByUser_Id(userId);

        return scores.stream()
                .map(ScoreHistoryDto::fromEntity)
                .collect(Collectors.toList());
    }




}
