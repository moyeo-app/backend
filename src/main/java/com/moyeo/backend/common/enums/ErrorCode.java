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
    INVALID_SORT_EXCEPTION(HttpStatus.BAD_REQUEST, "유효하지 않은 정렬 조건입니다."),
    // Kafka
    KAFKA_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "카프카 호출에 실패했습니다."),

    // Redis
    RIDES_SET_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "레디스 키 세팅에 실패했습니다." ),

    // OAuth
    OAUTH_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAuth API 호출에 실패했습니다."),
    INVALID_OAUTH_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 OAuth Access 토큰입니다."),

    // USER
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    OAUTH_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 OAuth ID 입니다."),
    ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 계좌번호입니다."),

    // Payment
    PAYMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 생성된 결제입니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),

    // Challenge
    INVALID_OPTION_FORMAT(HttpStatus.BAD_REQUEST, "챌린지 옵션 값이 유효하지 않습니다."),
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "챌린지 정보를 찾을 수 없습니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "해당 날짜는 선택 불가합니다."),
    // Challenge - Participation
    PARTICIPATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 처리된 챌린지 참여 요청입니다."),
    CHALLENGE_PARTICIPATION_CLOSED(HttpStatus.BAD_REQUEST, "챌린지 참여 인원 모집이 마감되었습니다."),
    NO_PENDING_RESERVATION(HttpStatus.BAD_REQUEST, "챌린지 참여 대기 상태가 아닙니다."),
    PARTICIPATION_NOT_FOUND(HttpStatus.NOT_FOUND, "챌린지 참여 정보를 찾을 수 없습니다."),
    // Challenge - Log
    CHALLENGE_NOT_IN_TIME(HttpStatus.BAD_REQUEST, "챌린지 키워드 입력 시간이 아닙니다."),
    CHALLENGE_OPTION_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "챌린지 타입과 옵션이 일치하지 않습니다."),
    CHALLENGE_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "챌린지 타입이 일치하지 않습니다."),
    CHALLENGE_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "챌린지 인증 정보를 찾을 수 없습니다."),
    CONTENT_KEYWORDS_NOT_FOUND(HttpStatus.BAD_REQUEST, "내용에 모든 키워드가 포함되어 있지 않습니다."),
    CHALLENGE_LOG_DATE_MISMATCH(HttpStatus.BAD_REQUEST, "챌린지 인증 날짜가 일치하지 않습니다."),
    CHALLENGE_LOG_CONTENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "챌린지 인증 내용 타입이 일치하지 않습니다."),

    // Routine
    // AI
    AI_BAD_RESPONSE(HttpStatus.BAD_REQUEST, "AI 응답 결과가 비어있거나, 필수 필드가 누락되었습니다."),
    AI_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI API 호출에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
