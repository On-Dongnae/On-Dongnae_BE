package com.semo.group1.on_dongnae.module.score.repository;

import com.semo.group1.on_dongnae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 단순 CURD 용도
}
