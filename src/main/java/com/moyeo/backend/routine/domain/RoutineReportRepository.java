package com.moyeo.backend.routine.domain;

import java.util.ArrayList;

public interface RoutineReportRepository {
    void upsertAll(ArrayList<RoutineReport> reports);

}
