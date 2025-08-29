package com.moyeo.backend.routine.infrastructure;

import com.moyeo.backend.routine.domain.RoutineReport;

import java.util.ArrayList;

public interface RoutineReportUpsertRepository {
    void upsertAll(ArrayList<RoutineReport> reports);
}
