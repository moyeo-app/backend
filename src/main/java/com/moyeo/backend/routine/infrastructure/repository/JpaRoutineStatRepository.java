package com.moyeo.backend.routine.infrastructure.repository;

import com.moyeo.backend.routine.domain.RoutineStat;
import com.moyeo.backend.routine.domain.RoutineStatRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaRoutineStatRepository extends
        RoutineStatRepository, JpaRepository<RoutineStat, UUID>,
        RoutineStatUpsertRepository {
}
