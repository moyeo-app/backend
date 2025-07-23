package com.moyeo.backend.challenge.basic.presentation.controller;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeResponseDto;
import com.moyeo.backend.challenge.basic.application.service.ChallengeService;
import com.moyeo.backend.common.request.PageRequestDto;
import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChallengeReadResponseDto>> getById(@PathVariable String id) {
        log.info("ChallengeId : {}", id);
        return ResponseEntity.ok().body(ApiResponse.success(challengeService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ChallengeReadResponseDto>>> gets(
            @ParameterObject @ModelAttribute ChallengeReadRequestDto requestDto,
            @ParameterObject @ModelAttribute PageRequestDto page) {
        log.info("Challenge Title : {}", requestDto.getTitle());
        log.info("page = {}, size = {}, sort = {}, direction = {}",
                page.getPage(), page.getSize(), page.getSort(), page.getDirection());

        return ResponseEntity.ok().body(ApiResponse.success(challengeService.gets(requestDto, page.toPageable())));
    }
}
