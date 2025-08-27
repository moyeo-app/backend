package com.moyeo.backend.routine.domain;

import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineStatRepository {
    void upsertAll(List<RoutineStatReadResponseDto> stats);

    Optional<RoutineStat> findByUserIdAndStartDateAndIsDeletedFalse(String userId, LocalDate startDate);
}
