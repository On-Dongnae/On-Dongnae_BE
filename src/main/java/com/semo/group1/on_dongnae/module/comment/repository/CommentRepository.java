package com.semo.group1.on_dongnae.module.comment.repository;

import com.semo.group1.on_dongnae.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFeedIdAndIsDeletedFalseOrderByCreatedAtAsc(Long feedId);
}
