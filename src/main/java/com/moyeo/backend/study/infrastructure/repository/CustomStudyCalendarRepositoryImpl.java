package com.moyeo.backend.study.infrastructure.repository;

import com.moyeo.backend.study.application.dto.DailyStudyDto;
import com.moyeo.backend.study.application.dto.QDailyStudyDto;
import com.moyeo.backend.study.application.dto.QWeeklyAgg;
import com.moyeo.backend.study.application.dto.WeeklyAgg;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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

    @Override
    public List<WeeklyAgg> findWeeklyAgg(LocalDate monday, LocalDate sunday) {

        NumberExpression<Integer> mon = dowSum(1);
        NumberExpression<Integer> tue = dowSum(2);
        NumberExpression<Integer> wed = dowSum(3);
        NumberExpression<Integer> thu = dowSum(4);
        NumberExpression<Integer> fri = dowSum(5);
        NumberExpression<Integer> sat = dowSum(6);
        NumberExpression<Integer> sun = dowSum(7);

        NumberExpression<Integer> totalMinutes = studyCalendar.totalMinutes.sum();
        NumberExpression<Integer> avgMinutes = totalMinutes.divide(7);
        NumberExpression<Integer> activeDays =
                new CaseBuilder().when(mon.gt(0)).then(1).otherwise(0)
                        .add(new CaseBuilder().when(tue.gt(0)).then(1).otherwise(0))
                        .add(new CaseBuilder().when(wed.gt(0)).then(1).otherwise(0))
                        .add(new CaseBuilder().when(thu.gt(0)).then(1).otherwise(0))
                        .add(new CaseBuilder().when(fri.gt(0)).then(1).otherwise(0))
                        .add(new CaseBuilder().when(sat.gt(0)).then(1).otherwise(0))
                        .add(new CaseBuilder().when(sun.gt(0)).then(1).otherwise(0));


        return jpaQueryFactory.select(new QWeeklyAgg(
                studyCalendar.user.id,
                Expressions.constant(monday),
                mon.coalesce(0), tue.coalesce(0), wed.coalesce(0),
                thu.coalesce(0), fri.coalesce(0), sat.coalesce(0), sun.coalesce(0),
                totalMinutes,
                avgMinutes,
                activeDays))
                .from(studyCalendar)
                .where(booleanBuilder(monday, sunday))
                .groupBy(studyCalendar.user.id)
                .fetch();
    }

    private NumberExpression<Integer> dowSum(int dow) {
        return Expressions.numberTemplate(Integer.class,
                "SUM(CASE WHEN CAST(function('date_part','isodow',{0}) AS int) = {1} THEN {2} ELSE 0 END)",
                studyCalendar.date, dow, studyCalendar.totalMinutes);
    }

    private BooleanBuilder booleanBuilder(String userId, LocalDate from, LocalDate to) {
        return  new BooleanBuilder()
                .and(isDeletedFalse())
                .and(eqUser(userId))
                .and(betweenDate(from, to));
    }

    private BooleanBuilder booleanBuilder(LocalDate monday, LocalDate sunday) {
        return  new BooleanBuilder()
                .and(isDeletedFalse())
                .and(betweenDate(monday, sunday));
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
