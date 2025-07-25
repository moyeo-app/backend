package com.moyeo.backend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "공통 RESPONSE DTO ")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T> (
        @Schema(description = "API 요청 상태 (success, fail)", example = "success")
        String status,
        
        @Schema(description = "상태 코드", example = "200")
        int code,

        @Schema(description = "응답 메시지", example = "API 요청이 성공했습니다.")
        String message,

        @Schema(description = "응답 데이터")
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

    public static ApiResponse<Object> fail(ResponseCode responseCode, List<String> errors) {
        return new  ApiResponse<>("fail", responseCode.getStatus().value(), responseCode.getMessage(), errors);
    }
}
