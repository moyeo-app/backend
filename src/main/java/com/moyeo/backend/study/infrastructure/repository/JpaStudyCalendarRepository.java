package com.moyeo.backend.study.infrastructure.repository;

import com.moyeo.backend.study.domain.StudyCalendar;
import com.moyeo.backend.study.domain.StudyCalendarRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaStudyCalendarRepository extends
        StudyCalendarRepository, JpaRepository<StudyCalendar, UUID>,
        StudyCalendarUpsertRepository, CustomStudyCalendarRepository {
}
