package com.moyeo.backend.challenge.participation.presentation;

import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.challenge.participation.application.service.ChallengeParticipationService;
import com.moyeo.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "ChallengeParticipationController")
@RestController
@RequestMapping("/v1/challenges")
@RequiredArgsConstructor
public class ChallengeParticipationController implements ChallengeParticipationControllerDocs {

    private final ChallengeParticipationService challengeParticipationService;

    @GetMapping("/{challengeId}/check")
    public ResponseEntity<ApiResponse<Boolean>> check(@PathVariable String challengeId) {
        return ResponseEntity.ok().body(ApiResponse.success(
                challengeParticipationService.check(challengeId)
        ));
    }

    @PostMapping("/{challengeId}/participates")
    public ResponseEntity<ApiResponse<Void>> participate(
            @PathVariable String challengeId,
            @Valid @RequestBody ChallengeParticipationRequestDto requestDto) {
        challengeParticipationService.participate(challengeId, requestDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
