package com.semo.group1.on_dongnae.module.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class SignUpRequest {

    private String email; // 클라이언트가 보낸 이메일
    private String password; // 클라이언트가 보낸 비밀번호
    private String nickname; // 클라이언트가 보낸 닉네임
    private Long regionId; //
}
