package com.moyeo.backend.challenge.participation.presentation;

import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadResponseDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationRequestDto;
import com.moyeo.backend.challenge.participation.application.service.ChallengeParticipationService;
import com.moyeo.backend.common.request.PageRequestDto;
import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponse<ChallengeParticipationReadResponseDto>>> gets(
            @ParameterObject @ModelAttribute ChallengeParticipationReadRequestDto requestDto,
            @ParameterObject @ModelAttribute PageRequestDto page) {
        return ResponseEntity.ok().body(ApiResponse.success(challengeParticipationService.gets(requestDto, page.toPageable())));
    }

    @PostMapping("/participates/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            challengeParticipationService.updateStatus(date);
        }
        log.info("(Admin) 챌린지 참여 상태 변경 Admin 으로 실행, from = {}, to = {}", from, to);
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
