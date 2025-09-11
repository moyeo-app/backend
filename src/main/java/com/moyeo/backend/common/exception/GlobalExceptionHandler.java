package com.moyeo.backend.common.exception;

import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "GlobalExceptionHandler")
@RestControllerAdvice(basePackages = "com.moyeo.backend")
public class GlobalExceptionHandler {

    /**
     * 예상하지 못한 모든 예외
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        log.warn("[Exception] 알 수 없는 서버 오류 발생", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    /**
     * 커스텀 예외 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        log.warn("[CustomException] {}: {}", responseCode.getStatus(), responseCode.getMessage());
        return ResponseEntity
                .status(responseCode.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.fail(responseCode));
    }

    /**
     * DB 제약 조건 위반
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataBaseException(DataIntegrityViolationException ex) {
        log.warn("[DataIntegrityViolationException] {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.fail(ErrorCode.INVALID_DB_REQUEST));
    }

    /**
     * DTO 유효성 검증 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn("[MethodArgumentNotValidException] {}", ex.getMessage());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.fail(ErrorCode.VALIDATION_EXCEPTION, errors));
    }

    /**
     * 존재하지 않는 URL
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NoHandlerFoundException ex) {
        log.warn("[NoHandlerFoundException] {}", ex.getRequestURL());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.fail(ErrorCode.NOT_FOUND));
    }

    /**
     * 잘못된 HTTP 메서드 요청
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.warn("[HttpRequestMethodNotSupportedException] {}", ex.getMethod());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.fail(ErrorCode.METHOD_NOT_ALLOWED));
    }

    /**
     * 요청 Body 가 없을 때
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmptyRequestBody(HttpMessageNotReadableException ex) {
        log.warn("[HttpMessageNotReadableException] {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON) // ★
                .body(ApiResponse.fail(ErrorCode.BAD_REQUEST));
    }

    /**
     * 클라이언트 중단 계열 → 바디 없이 종료
     */
    @ExceptionHandler({ ClientAbortException.class, AsyncRequestNotUsableException.class })
    public ResponseEntity<Void> handleClientAbortLight(Exception ex) {
        log.warn("[ClientAbort] {}", ex.toString());
        return ResponseEntity.noContent().build();
    }

    /**
     * I/O 예외: broken pipe/connection reset 은 noContent, 그 외는 500 JSON
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        String msg = String.valueOf(ex.getMessage()).toLowerCase();
        if (msg.contains("broken pipe") || msg.contains("connection reset")) {
            log.warn("[IO-BrokenPipe] {}", ex.toString());
            return ResponseEntity.noContent().build();
        }
        log.warn("[I/O] {}", ex.toString());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
