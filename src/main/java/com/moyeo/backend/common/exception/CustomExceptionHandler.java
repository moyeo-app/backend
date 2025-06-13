package com.moyeo.backend.common.exception;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "CustomExceptionHandler")
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomPaymentException(CustomException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        return ResponseEntity
                .status(responseCode.getStatus())
                .body(ApiResponse.fail(responseCode));
    }
}
