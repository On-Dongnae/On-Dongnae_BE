package com.semo.group1.on_dongnae.module.user.service;

import com.semo.group1.on_dongnae.entity.Region;
import com.semo.group1.on_dongnae.entity.enums.Role;
import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.global.aws.S3UploadService;
import com.semo.group1.on_dongnae.global.exception.CustomException;
import com.semo.group1.on_dongnae.global.exception.ErrorCode;
import com.semo.group1.on_dongnae.global.jwt.JwtUtil;
import com.semo.group1.on_dongnae.global.security.SecurityUtil;
import com.semo.group1.on_dongnae.module.region.repository.RegionRepository;
import com.semo.group1.on_dongnae.module.user.dto.LogInRequest;
import com.semo.group1.on_dongnae.module.user.dto.SignUpRequest;
import com.semo.group1.on_dongnae.module.user.dto.UserProfileDto;
import com.semo.group1.on_dongnae.module.user.dto.UserProfileUpdateDto;
import com.semo.group1.on_dongnae.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SecurityUtil securityUtil;
    private final S3UploadService s3UploadService;

    // 1. 회원가입
    public void signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동네입니다."));

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .region(region)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    // 2. 로그인
    public String login(LogInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getEmail());
    }

    // 3. 내 프로필 조회
    @Transactional(readOnly = true)
    public UserProfileDto getMyProfile() {
        User user = securityUtil.getCurrentUser();
        return UserProfileDto.fromEntity(user);
    }

    // 4. 내 프로필 수정
    @Transactional
    public UserProfileDto updateMyProfile(UserProfileUpdateDto request) {
        User user = securityUtil.getCurrentUser();

        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            user.updateNickname(request.getNickname());
        }

        return UserProfileDto.fromEntity(user);
    }

    // 5. 프로필 이미지 변경
    @Transactional
    public UserProfileDto updateProfileImage(MultipartFile image) {
        User user = securityUtil.getCurrentUser();

        try {
            String imageUrl = s3UploadService.uploadFile(image, "profiles/" + user.getId());
            user.updateProfileImage(imageUrl);
        } catch (IOException e) {
            log.error("프로필 이미지 업로드 중 오류 발생", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return UserProfileDto.fromEntity(user);
    }
}

