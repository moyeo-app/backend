package com.moyeo.backend.routine.infrastructure;

import com.moyeo.backend.routine.domain.RoutineReport;
import com.moyeo.backend.routine.domain.RoutineReportRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaRoutineReportRepository extends
        RoutineReportRepository, JpaRepository<RoutineReport, UUID>,
        RoutineReportUpsertRepository {
}
