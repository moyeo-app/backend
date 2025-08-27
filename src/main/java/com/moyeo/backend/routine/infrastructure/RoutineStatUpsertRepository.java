package com.moyeo.backend.routine.infrastructure;

import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;

import java.util.List;

public interface RoutineStatUpsertRepository {
    void upsertAll(List<RoutineStatReadResponseDto> stats);

}
