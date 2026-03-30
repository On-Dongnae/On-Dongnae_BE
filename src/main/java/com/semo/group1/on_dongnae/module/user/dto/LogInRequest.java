package com.semo.group1.on_dongnae.module.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class LogInRequest {
    private String email;
    private String password;
}
