package com.moyeo.backend.challenge.basic.presentation.controller;

import com.moyeo.backend.challenge.basic.application.service.ChallengeService;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;
import com.moyeo.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "ChallengeController")
@RestController
@RequestMapping("/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController implements ChallengeControllerDocs{

    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChallengeResponseDto>> create(@Valid @RequestBody ChallengeCreateRequestDto requestDto) {
        log.info("PaymentId : {}", requestDto.getPaymentId());
        return ResponseEntity.ok().body(ApiResponse.success(challengeService.create(requestDto)));
    }
}
