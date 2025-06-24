package com.moyeo.backend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 RESPONSE DTO ")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T> (
        String status,
        int code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", 200, "API 요청이 성공했습니다.", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>("success", 200, "API 요청이 성공했습니다.", null);
    }

    public static ApiResponse<Object> fail(ResponseCode responseCode) {
        return new ApiResponse<>("fail", responseCode.getStatus().value(), responseCode.getMessage(), null);
    }
}
