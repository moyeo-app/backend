package com.moyeo.backend.common.exception;

import com.moyeo.backend.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ResponseCode responseCode;
}
