package com.moyeo.backend.routine.infrastructure.repository;

import com.moyeo.backend.routine.domain.RoutineReport;

import java.util.List;

public interface RoutineReportUpsertRepository {
    void upsertAll(List<RoutineReport> reports);
}
