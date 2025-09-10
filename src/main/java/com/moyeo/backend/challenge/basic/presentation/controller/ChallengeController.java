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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/{challengeId}")
    public ResponseEntity<ApiResponse<ChallengeReadResponseDto>> getById(@PathVariable String challengeId) {
        log.info("ChallengeId : {}", challengeId);
        return ResponseEntity.ok().body(ApiResponse.success(challengeService.getById(challengeId)));
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

    @PostMapping("/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            challengeService.updateStatus(date);
        }
        log.info("(Admin) 챌린지 상태 변경 Admin 으로 실행, from = {}, to = {}", from, to);
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
