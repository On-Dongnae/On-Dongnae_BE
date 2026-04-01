package com.semo.group1.on_dongnae.module.score.cache;

import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class RankingCache {
    // DB 반복 조회 방지
    private volatile List<UserRanking> userRankings = new ArrayList<>();
    private volatile List<RegionRanking> regionRankings = new ArrayList<>();

    // 랭킹 초기화 스캐줄 수정 불가 리스트로 복사
    public void updateUserRankings(List<UserRanking> rankings) {
        this.userRankings = Collections.unmodifiableList(new ArrayList<>(rankings));

    }

    public void updateRegionRankings(List<RegionRanking> rankings) {
        this.regionRankings = Collections.unmodifiableList(new ArrayList<>(rankings));
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

}
