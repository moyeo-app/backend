package com.moyeo.backend.challenge.log.infrastructure.repository;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogDailyAggregateDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadRequestDto;
import com.moyeo.backend.challenge.log.application.dto.ChallengeLogReadResponseDto;
import com.moyeo.backend.challenge.log.application.dto.QChallengeLogDailyAggregateDto;
import com.moyeo.backend.challenge.log.application.mapper.ChallengeLogMapper;
import com.moyeo.backend.challenge.log.domain.ChallengeLog;
import com.moyeo.backend.challenge.log.domain.ChallengeLogStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.moyeo.backend.challenge.log.domain.QChallengeLog.challengeLog;
import static com.moyeo.backend.challenge.participation.domain.QChallengeParticipation.challengeParticipation;
import static com.moyeo.backend.common.util.QueryDslSortUtil.getOrderSpecifiers;
import static com.moyeo.backend.user.domain.QUser.user;

@RequiredArgsConstructor
public class CustomChallengeLogRepositoryImpl implements CustomChallengeLogRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ChallengeLogMapper logMapper;


    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
            "createdAt", challengeLog.createdAt
    );

    @Override
    public Page<ChallengeLogReadResponseDto> getLogs(String challengeId, ChallengeLogReadRequestDto requestDto, Pageable pageable) {

        BooleanBuilder booleanBuilder = booleanBuilder(challengeId, requestDto);

        JPAQuery<ChallengeLog> logs = jpaQueryFactory
                .selectFrom(challengeLog)
                .join(challengeLog.participation ,challengeParticipation).fetchJoin()
                .join(challengeParticipation.user, user).fetchJoin()
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS));

        JPAQuery<Long> total = jpaQueryFactory
                .select(challengeLog.count())
                .from(challengeLog)
                .join(challengeLog.participation ,challengeParticipation)
                .join(challengeParticipation.user, user)
                .where(booleanBuilder);

        List<ChallengeLogReadResponseDto> results = logs.fetch().stream()
                .map(logMapper::toLogDto)
                .toList();

        return PageableExecutionUtils.getPage(
                results,
                pageable,
                total::fetchOne
        );
    }

    @Override
    public List<ChallengeLogDailyAggregateDto> aggregateDailyByUser(LocalDate date) {

        BooleanBuilder booleanBuilder = booleanBuilder(date);

        NumberExpression<Long> minutesPerLog = Expressions.numberTemplate(
                Long.class,
                "cast(floor( (cast(function('date_part','epoch', function('age', {0}, {1})) as big_decimal)) / 60.0 ) as long)",
                challengeLog.updatedAt, challengeLog.createdAt
        );

        NumberExpression<Long> totalMinutes = minutesPerLog.sum().coalesce(0L);

        return jpaQueryFactory.select(new QChallengeLogDailyAggregateDto(
                challengeParticipation.user.id,
                        challengeLog.date,
                        totalMinutes
                ))
                .from(challengeLog)
                .join(challengeLog.participation, challengeParticipation)
                .join(challengeParticipation.user, user)
                .where(booleanBuilder)
                .groupBy(challengeParticipation.user.id, challengeLog.date)
                .fetch();
    }

    private BooleanBuilder booleanBuilder(String challengeId, ChallengeLogReadRequestDto requestDto) {

        return new BooleanBuilder()
                .and(isDeletedFalse())
                .and(challengeIsDeletedFalse())
                .and(eqChallenge(challengeId))
                .and(eqStatus(requestDto.getStatus()));
    }

    private BooleanBuilder booleanBuilder(LocalDate date) {

        return new BooleanBuilder()
                .and(isDeletedFalse())
                .and(challengeIsDeletedFalse())
                .and(eqDate(date))
                .and(eqStatus(ChallengeLogStatus.SUCCESS));
    }

    private BooleanExpression isDeletedFalse() {
        return challengeLog.isDeleted.isFalse();
    }

    private BooleanExpression challengeIsDeletedFalse() {
        return challengeLog.challenge.isDeleted.isFalse();
    }

    private BooleanExpression eqChallenge(String challengeId) {
        return challengeLog.challenge.id.eq(challengeId);
    }

    private BooleanExpression eqStatus(ChallengeLogStatus status) {
        return status != null ? challengeLog.status.eq(status) : null;
    }

    private BooleanExpression eqDate(LocalDate date) {
        return challengeLog.date.eq(date);
    }
}