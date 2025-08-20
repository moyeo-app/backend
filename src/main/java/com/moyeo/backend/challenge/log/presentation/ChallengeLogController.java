package com.moyeo.backend.challenge.log.presentation;

import com.moyeo.backend.challenge.log.application.dto.*;
import com.moyeo.backend.challenge.log.application.service.ChallengeLogService;
import com.moyeo.backend.common.request.PageRequestDto;
import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "ChallengeLogController")
@RestController
@RequestMapping("/v1/challenges")
@RequiredArgsConstructor
public class ChallengeLogController implements  ChallengeLogControllerDocs{

    private final ChallengeLogService challengeLogService;

    @PostMapping("/{challengeId}/keywords")
    public ResponseEntity<ApiResponse<ChallengeLogResponseDto>> create(
            @PathVariable String challengeId,
            @Valid @RequestBody ChallengeLogKeywordRequestDto requestDto) {
        return ResponseEntity.ok().body(ApiResponse.success(
                challengeLogService.create(challengeId, requestDto)
        ));
    }

    @PatchMapping("/{challengeId}/logs/{logId}/text")
    public ResponseEntity<ApiResponse<ChallengeLogResponseDto>> update(
            @PathVariable String challengeId,
            @PathVariable String logId,
            @Valid @RequestBody ChallengeLogContentRequestDto requestDto) {
        return ResponseEntity.ok().body(ApiResponse.success(
                challengeLogService.update(challengeId, logId, requestDto)
        ));
    }

    @GetMapping("/{challengeId}/logs")
    public ResponseEntity<ApiResponse<PageResponse<ChallengeLogReadResponseDto>>> gets(
            @PathVariable String challengeId,
            @ParameterObject @ModelAttribute ChallengeLogReadRequestDto requestDto,
            @ParameterObject @ModelAttribute PageRequestDto page) {
        return ResponseEntity.ok().body(ApiResponse.success(
                challengeLogService.gets(challengeId, requestDto, page.toPageable())));
    }

    @GetMapping("/{challengeId}/logs/me")
    public ResponseEntity<ApiResponse<ChallengeLogReadResponseDto>> getByUser(@PathVariable String challengeId) {
        return ResponseEntity.ok().body(ApiResponse.success(
                challengeLogService.getByUser(challengeId)
        ));
    }
}
