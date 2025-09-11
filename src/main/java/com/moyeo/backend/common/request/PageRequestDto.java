package com.moyeo.backend.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "챌린지 목록 REQUEST PARAMETER DTO")
public class PageRequestDto {

    @Schema(description = "페이지 번호", defaultValue = "1", example = "1")
    @Min(1)
    private Integer page;

    @Schema(description = "페이지에 포함할 항목 수", defaultValue = "10", example = "10")
    @Min(1)
    @Max(50)
    private Integer size;

    @Schema(description = "정렬 기준", example = "createdAt")
    private String sort;

    @Schema(description = "정렬 방향", defaultValue = "desc", example = "desc", allowableValues = {"asc", "desc"})
    private String direction;

    public Pageable toPageable() {
        int finalPage = (page != null && page > 0) ? page - 1 : 0;
        int finalSize = (size != null && size > 0) ? size : 10;
        String finalSort = (sort != null && !sort.isBlank()) ? sort : "createdAt";
        String finalDirection = (direction != null && direction.equalsIgnoreCase("asc")) ? "asc" : "desc";

        return PageRequest.of(finalPage, finalSize, Sort.by(Sort.Direction.fromString(finalDirection), finalSort));
    }
}
