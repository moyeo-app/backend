package com.moyeo.backend.challenge.log.presentation;

import com.moyeo.backend.challenge.log.application.service.ChallengeLogService;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogKeywordRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogResponseDto;
import com.moyeo.backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            @RequestBody ChallengeLogKeywordRequestDto requestDto) {
        return ResponseEntity.ok().body(ApiResponse.success(
                challengeLogService.create(challengeId, requestDto)
        ));
    }
}
