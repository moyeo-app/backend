package com.moyeo.backend.study.application.validator;

import com.moyeo.backend.study.application.dto.DateRange;
import com.moyeo.backend.study.application.dto.StudyCalendarReadRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StudyCalendarValidator {

    private final Clock clock;

    public DateRange rangeForCalendar(StudyCalendarReadRequestDto requestDto) {
        LocalDate cap = LocalDate.now(clock).minusDays(1);

        LocalDate to = Optional.ofNullable(requestDto.getTo()).orElse(cap);
        if (to.isAfter(cap)) to = cap;

        LocalDate from = Optional.ofNullable(requestDto.getFrom()).orElse(to.withDayOfMonth(1));
        return from.isAfter(to) ? new DateRange(null, null) : new DateRange(from, to);
    }
}
