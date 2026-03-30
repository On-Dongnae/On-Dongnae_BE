package com.semo.group1.on_dongnae.module.user.service;

import com.semo.group1.on_dongnae.entity.Region;
import com.semo.group1.on_dongnae.entity.enums.Role;
import com.semo.group1.on_dongnae.entity.User;
import com.semo.group1.on_dongnae.global.jwt.JwtUtil;
import com.semo.group1.on_dongnae.module.region.repository.RegionRepository;
import com.semo.group1.on_dongnae.module.user.dto.LogInRequest;
import com.semo.group1.on_dongnae.module.user.dto.SignUpRequest;
import com.semo.group1.on_dongnae.module.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service

public class UserService {


    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    // 보안 점검
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, RegionRepository regionRepository
            , PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 1. 회원가입 (SignUp) 로직


    public void signUp(SignUpRequest request) {

        // 1-1. 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            // 중복되었을 경우
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 1-2. 프론트에서 받은 regionId로 DB에서 실제 동네(Region) 정보 찾아오기
        Region region = regionRepository.findById(request.getRegionId())
                // 동네 정보가 없을 경우
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동네입니다."));

        // 1-3. 비밀번호 암호화 (암호화된 상태로 DB에 저장, Bcrypt 암호화)
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 1-4. 이메일, 비밀번호, 닉네임, 지역, 역할에 대한 데이터를 실제 DB에 넣을 User 엔티티 만들기 (Builder 사용)
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword()) // 시큐리티 임시 해제 상태이므로 그대로 저장
                .nickname(request.getNickname())
                .region(region)
                .role(Role.USER)
                .build();

        // 1-4. DB에 마지막으로 저장
        userRepository.save(user);
    }

    // 2. 로그인 (LogIn) 로직


    public String login(LogInRequest request) {

        // 2-1. DB에서 전달받은 이메일로 유저가 있는지 검색
        User user = userRepository.findByEmail(request.getEmail())
                // 이메일을 찾을 수 없을 경우
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 2-2. 찾아온 유저의 DB 비밀번호와, 방금 입력한 로그인 비밀번호(암호화 후)가 일치하는지 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 2-3. 모두 통과 시 성공 메시지 및 JWT 토큰 반환
        return jwtUtil.createToken(user.getEmail());
    }
}
