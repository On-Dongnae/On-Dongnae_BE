package com.semo.group1.on_dongnae.module.score.cache;

import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class RankingCache {
    // DB 반복 조회 방지
    private volatile List<UserRanking> userRankings = new ArrayList<>();
    private volatile List<RegionRanking> regionRankings = new ArrayList<>();
    // API 대비 랭킹 저장 변수 선언

    private volatile LocalDateTime lastUserUpdate; // 마지막 갱신 시간
    private volatile int lastUserUpdateCount; // 최신 데이터 수 추가

    private volatile LocalDateTime lastRegionUpdate;
    private volatile int lastRegionUpdateCount;

    // 랭킹 초기화 스캐줄 수정 불가 리스트로 복사
    public void updateUserRankings(List<UserRanking> rankings) {
        this.userRankings = Collections.unmodifiableList(new ArrayList<>(rankings));
        this.lastUserUpdate = LocalDateTime.now();
        this.lastUserUpdateCount = rankings.size();

    }

    public void updateRegionRankings(List<RegionRanking> rankings) {
        this.regionRankings = Collections.unmodifiableList(new ArrayList<>(rankings));
        this.lastRegionUpdate = LocalDateTime.now();
        this.lastRegionUpdateCount = rankings.size();
    }

    // 서비스에서 데이터를 가져갈 때 for 상위 n명 유저
    public List<UserRanking> getUserRankings(int n) {
        if (n>=userRankings.size()) return userRankings;
        return userRankings.subList(0, n);
    }
    // 서비스에서 데이터를 가져갈 때 for 상위 n개 동네
    public List<RegionRanking> getRegionRankings(int n) {
        if (n>= regionRankings.size()) return regionRankings;
        return regionRankings.subList(0, n);
    }

    // 개인 최신 데이터 for API
    public LocalDateTime getLastUserUpdate() { return lastUserUpdate; }
    public int getLastUserUpdateCount() { return lastUserUpdateCount; }



    // 동네 최신 데이터 for API
    public LocalDateTime getLastRegionUpdate() { return lastRegionUpdate; }
    public int getLastRegionUpdateCount() { return lastRegionUpdateCount; }





}
