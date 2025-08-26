package com.moyeo.backend.study.application.dto;

import java.time.LocalDate;

public record DateRange(
        LocalDate from,
        LocalDate to
) {
}
