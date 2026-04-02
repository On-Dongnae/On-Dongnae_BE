package com.semo.group1.on_dongnae.module.feed.repository;

import com.semo.group1.on_dongnae.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByIsDeletedFalseOrderByCreatedAtDesc();
}
