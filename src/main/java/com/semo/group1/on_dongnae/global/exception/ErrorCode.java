package com.semo.group1.on_dongnae.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT("잘못된 입력값입니다.", 400),

    // 401 Unauthorized
    UNAUTHORIZED("인증이 필요합니다.", 401),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", 401),
    EXPIRED_TOKEN("만료된 토큰입니다.", 401),

    // 403 Forbidden
    ACCESS_DENIED("접근 권한이 없습니다.", 403),

    // 404 Not Found
    USER_NOT_FOUND("존재하지 않는 사용자입니다.", 404),
    REGION_NOT_FOUND("존재하지 않는 동네입니다.", 404),
    MISSION_NOT_FOUND("존재하지 않는 미션입니다.", 404),
    USER_MISSION_NOT_FOUND("배정된 미션을 찾을 수 없습니다.", 404),
    FEED_NOT_FOUND("존재하지 않는 피드입니다.", 404),
    COMMENT_NOT_FOUND("존재하지 않는 댓글입니다.", 404),
    VERIFICATION_NOT_FOUND("존재하지 않는 인증 정보입니다.", 404),

    // 409 Conflict
    DUPLICATE_EMAIL("이미 가입된 이메일입니다.", 409),
    ALREADY_VERIFIED("이미 인증된 미션입니다.", 409),
    ALREADY_ASSIGNED("해당 일자에 이미 미션을 배정받았습니다.", 409),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", 500);

    private final String message;
    private final int status;
}
