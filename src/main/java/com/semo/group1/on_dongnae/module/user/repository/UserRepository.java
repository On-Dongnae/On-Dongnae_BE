package com.semo.group1.on_dongnae.module.user.repository;

import com.semo.group1.on_dongnae.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<다룰엔티티, PK타입> 을 상속받으면 기본기능(저장, 조회)이 자동 완성됩니다.
public interface UserRepository extends JpaRepository<User, Long> {

    // 가입 시 이메일이 이미 존재하는지 중복 여부를 검사
    boolean existsByEmail(String email);

    // 추후 로그인 시 이메일로 회원을 조회
    Optional<User> findByEmail(String email);
}
