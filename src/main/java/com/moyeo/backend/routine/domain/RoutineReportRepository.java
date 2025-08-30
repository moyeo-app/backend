package com.moyeo.backend.routine.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoutineReportRepository {
    void upsertAll(List<RoutineReport> reports);

    Optional<RoutineReport> findByUserIdAndStartDateAndIsDeletedFalse(String userId, LocalDate startDate);
}
