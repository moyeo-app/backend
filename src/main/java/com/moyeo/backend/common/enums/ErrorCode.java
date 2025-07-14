package com.moyeo.backend.common.enums;

import com.moyeo.backend.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResponseCode {

    // Common
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 입력되지 않았습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "입력 유효성 검증에 실패했습니다."),
    INVALID_DB_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 데이터 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 URL 을 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),


    // OAuth
    OAUTH_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth API 호출에 실패했습니다."),
    INVALID_OAUTH_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 OAuth Access 토큰입니다."),

    // USER
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    OAUTH_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 OAuth ID 입니다."),
    ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 계좌번호입니다."),

    // Payment
    PAYMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 생성된 결제입니다."),
    PAYMENT_NOT_FOUND(HttpStatus.CONFLICT, "결제 정보를 찾을 수 없습니다."),

    // Challenge
    INVALID_OPTION_FORMAT(HttpStatus.BAD_REQUEST, "챌린지 옵션 값이 유효하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
