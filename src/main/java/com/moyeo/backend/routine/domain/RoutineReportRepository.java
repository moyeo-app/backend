package com.moyeo.backend.routine.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public interface RoutineReportRepository {
    void upsertAll(ArrayList<RoutineReport> reports);

    Optional<RoutineReport> findByUserIdAndStartDateAndIsDeletedFalse(String userId, LocalDate startDate);
}
