package com.moyeo.backend.common.enums;

import com.moyeo.backend.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResponseCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // OAuth
    OAUTH_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth API 호출에 실패했습니다."),
    EXPIRED_OAUTH_TOKEN(HttpStatus.BAD_REQUEST, "만료된 OAuth Access 토큰입니다."),

    // USER
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");

    private final HttpStatus status;
    private final String message;
}
