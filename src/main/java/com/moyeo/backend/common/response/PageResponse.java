package com.moyeo.backend.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "목록 RESPONSE DTO")
public class PageResponse<T> {

    @Schema(description = "페이지 데이터 목록")
    private List<T> content;

    @Schema(description = "현재 페이지 번호 (1부터 시작)")
    private int pageNumber;

    @Schema(description = "페이지당 항목 수")
    private int pageSize;

    @Schema(description = "전체 페이지 수")
    private int totalPages;

    @Schema(description = "전체 항목 수")
    private long totalElements;

    @Schema(description = "마지막 페이지 여부")
    private boolean last;
}
