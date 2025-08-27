package com.moyeo.backend.routine.application.service;

import com.moyeo.backend.routine.application.dto.RoutineStatReadRequestDto;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;

public interface RoutineService {

    RoutineStatReadResponseDto getRoutineStat(RoutineStatReadRequestDto requestDto);
}
