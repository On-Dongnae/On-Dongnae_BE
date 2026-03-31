package com.semo.group1.on_dongnae.global.security;

import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;

    /**
     * 현재 로그인한 사용자의 이메일을 SecurityContext에서 추출합니다.
     * JWT 필터에서 Authentication의 principal로 이메일을 저장해두었으므로 그대로 꺼냅니다.
     */
    public String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return (String) authentication.getPrincipal();
    }

    /**
     * 현재 로그인한 사용자의 User 엔티티를 DB에서 조회하여 반환합니다.
     * 대부분의 서비스 로직에서 이 메서드를 사용하면 됩니다.
     */
    public User getCurrentUser() {
        String email = getCurrentEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
