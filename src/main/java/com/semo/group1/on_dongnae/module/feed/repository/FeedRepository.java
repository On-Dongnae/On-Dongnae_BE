package com.semo.group1.on_dongnae.module.feed.repository;

import com.semo.group1.on_dongnae.entity.Feed;
import com.semo.group1.on_dongnae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByIsDeletedFalseOrderByCreatedAtDesc();
    
    // 1. 내가 작성한 글
    List<Feed> findByUserAndIsDeletedFalseOrderByCreatedAtDesc(User user);
    
    // 2. 내가 댓글을 작성한 글 (중복 제거)
    @Query("SELECT DISTINCT f FROM Feed f JOIN f.comments c WHERE c.user = :user AND f.isDeleted = false AND c.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feed> findMyCommentedFeeds(@Param("user") User user);
}
