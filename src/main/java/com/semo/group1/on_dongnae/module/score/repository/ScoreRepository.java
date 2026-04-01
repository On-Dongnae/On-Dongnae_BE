package com.semo.group1.on_dongnae.module.score.repository;

import com.semo.group1.on_dongnae.module.score.dto.UserRanking;
import com.semo.group1.on_dongnae.module.score.dto.RegionRanking;
import com.semo.group1.on_dongnae.entity.Score;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
public interface ScoreRepository extends JpaRepository<Score, Long> {
    // 개인 랭킹 Query
    @Query("SELECT new com.semo.group1.on_dongnae.module.dto.UserRanking(s.user.userId, s.user.nickname, SUM(s.amount)) "
            + "FROM Score s "
            + "Where s.createdAt >= :startDate And s.createdAt < : endDate "
            + "GROUP BY s.user.userId, s.user.nickname "
            + "ORDER BY SUM(s.amount) DESC")
    List<UserRanking>findUserRankings(
            @Param("StartDate")LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // 동네 랭킹 Query
    @Query ("SELECT new com.semo.group1.on_dongnae.module.dto.RegionRanking(s.region.regionId, s.region.regioName, SUM(s.amount)) "
            + "FROM Score s "
            + "Where s.createdAt >= : startDAte And s.createdAt < : endDate "
            + "GROUP BY s.region.regionId, s.region.regionName "
            + "ORDER BY SUM(s.amount) DESC")
    List<RegionRanking>findRegionRankings(
            @Param("StartDate")LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // 내 순위 Query
    @Query(value = "SELECT user_rank.rnk FROM ("
            + "  SELECT user_id, RANK() OVER (ORDER BY SUM(amount) DESC) as rnk "
            + "  FROM scores "
            + "  WHERE created_at >= : startDate AND created_at < : endDate "
            + " GROUP BY user_id"
            + " user_rank WHERE user_rank.user_id = :userId", nativeQuery = true)
    Long findMyRank(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}



