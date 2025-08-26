package com.moyeo.backend.study.infrastructure.repository;

import com.moyeo.backend.study.application.dto.DailyStudyDto;
import com.moyeo.backend.study.application.dto.QDailyStudyDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.moyeo.backend.study.domain.QStudyCalendar.studyCalendar;

@RequiredArgsConstructor
public class CustomStudyCalendarRepositoryImpl implements CustomStudyCalendarRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DailyStudyDto> findDailyTotals(String userId, LocalDate from, LocalDate to) {
        BooleanBuilder booleanBuilder = booleanBuilder(userId, from, to);

        return jpaQueryFactory.select(new QDailyStudyDto(
                studyCalendar.date,
                studyCalendar.totalMinutes))
                .from(studyCalendar)
                .where(booleanBuilder)
                .orderBy(studyCalendar.date.asc())
                .fetch();
    }

    private BooleanBuilder booleanBuilder(String userId, LocalDate from, LocalDate to) {
        return  new BooleanBuilder()
                .and(isDeletedFalse())
                .and(eqUser(userId))
                .and(betweenDate(from, to));
    }

    private BooleanExpression isDeletedFalse() {
        return studyCalendar.isDeleted.isFalse();
    }

    private BooleanExpression eqUser(String userId) {
        return studyCalendar.user.id.eq(userId);
    }

    private BooleanExpression betweenDate(LocalDate from, LocalDate to) {
        return studyCalendar.date.between(from, to);
    }

}
