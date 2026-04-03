package com.semo.group1.on_dongnae.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final int status;
    private final String message;
    private final T data;

    // 성공 응답 (데이터 포함)
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .build();
    }

    // 성공 응답 (커스텀 메시지 + 데이터)
    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    // 성공 응답 (데이터 없이 메시지만)
    public static ApiResponse<Void> ok(String message) {
        return ApiResponse.<Void>builder()
                .status(200)
                .message(message)
                .build();
    }

    // 생성 성공 응답 (201)
    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .status(201)
                .message("리소스가 성공적으로 생성되었습니다.")
                .data(data)
                .build();
    }

    public static ApiResponse<Void> created(String message) {
        return ApiResponse.<Void>builder()
                .status(201)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .status(201)
                .message(message)
                .data(data)
                .build();
    }

    // 에러 응답
    public static ApiResponse<Void> error(int status, String message) {
        return ApiResponse.<Void>builder()
                .status(status)
                .message(message)
                .build();
    }
}
