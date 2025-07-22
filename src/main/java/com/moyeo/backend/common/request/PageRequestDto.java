package com.moyeo.backend.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 목록 REQUEST PARAMETER DTO")
public class PageRequestDto {

    @Schema(description = "페이지 번호", defaultValue = "1", example = "1")
    @Min(1)
    private Integer page = 1;

    @Schema(description = "페이지에 포함할 항목 수", defaultValue = "10", example = "10")
    @Min(1)
    @Max(50)
    private Integer size = 10;

    @Schema(description = "정렬 필드, 콤마로 구분", example = "createdAt,desc")
    private String sort = "createdAt,desc";

    public Pageable toPageable() {
        int zeroBasePage = Math.max(this.page - 1, 0);
        String[] sortParts = sort.split(",");
        return PageRequest.of(zeroBasePage, size, Sort.by(Sort.Direction.fromString(sortParts[1]), sortParts[0]));
    }
}
