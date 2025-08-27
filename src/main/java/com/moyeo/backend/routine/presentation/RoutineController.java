package com.moyeo.backend.routine.presentation;

import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.routine.application.service.RoutineService;
import com.moyeo.backend.routine.application.dto.RoutineStatReadRequestDto;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "RoutineStatController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routines")
public class RoutineController implements RoutineControllerDocs {

    private final RoutineService routineService;

    @GetMapping("/stat/me")
    public ResponseEntity<ApiResponse<RoutineStatReadResponseDto>> getRoutineStat(
            @ParameterObject @ModelAttribute RoutineStatReadRequestDto requestDto) {
        return ResponseEntity.ok().body(ApiResponse.success(routineService.getRoutineStat(requestDto)));
    }
}
