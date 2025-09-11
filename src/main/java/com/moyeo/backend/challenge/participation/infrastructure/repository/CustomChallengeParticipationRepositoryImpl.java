package com.moyeo.backend.challenge.participation.infrastructure.repository;

import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadResponseDto;
import com.moyeo.backend.challenge.participation.application.mapper.ChallengeParticipationMapper;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.challenge.participation.domain.enums.ParticipationStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.moyeo.backend.challenge.basic.domain.QChallenge.challenge;
import static com.moyeo.backend.challenge.participation.domain.QChallengeParticipation.challengeParticipation;
import static com.moyeo.backend.common.util.QueryDslSortUtil.getOrderSpecifiers;
import static com.moyeo.backend.routine.domain.QRoutineStat.routineStat;

@Slf4j(topic = "CustomChallengeParticipationRepository")
@RequiredArgsConstructor
public class CustomChallengeParticipationRepositoryImpl implements CustomChallengeParticipationRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    private final ChallengeParticipationMapper participationMapper;
    private final Clock clock;

    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
            "createdAt", challengeParticipation.createdAt
    );

    @Override
    public Page<ChallengeParticipationReadResponseDto> findMyParticipation(String userId, ChallengeParticipationReadRequestDto requestDto, Pageable pageable) {

        BooleanBuilder booleanBuilder = booleanBuilder(userId, requestDto);

        JPAQuery<ChallengeParticipation> participation = jpaQueryFactory
                .selectFrom(challengeParticipation)
                .join(challengeParticipation.challenge, challenge).fetchJoin()
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS));

        JPAQuery<Long> total = jpaQueryFactory
                .select(challengeParticipation.count())
                .from(challengeParticipation)
                .join(challengeParticipation.challenge, challenge)
                .where(booleanBuilder);

        List<ChallengeParticipationReadResponseDto> results = participation.fetch().stream()
                .map(participationMapper::toParticipantDto)
                .toList();

        return PageableExecutionUtils.getPage(
                results,
                pageable,
                total::fetchOne
        );
    }

    @Override
    public void updateStatus(LocalDate date) {

        long toInProgress = jpaQueryFactory
                .update(challengeParticipation)
                .set(challengeParticipation.status, ParticipationStatus.INPROGRESS)
                .set(challengeParticipation.updatedAt, LocalDateTime.now(clock))
                .where(isDeletedFalse()
                                .and(challengeParticipation.status.isNull())
                                .and(challengeParticipation.challenge.startDate.loe(date)))
                .execute();

        long toEnd = jpaQueryFactory
                .update(challengeParticipation)
                .set(challengeParticipation.status, ParticipationStatus.END)
                .set(challengeParticipation.updatedAt, LocalDateTime.now(clock))
                .where(isDeletedFalse()
                                .and(challengeParticipation.status.eq(ParticipationStatus.INPROGRESS))
                                .and(challengeParticipation.challenge.endDate.lt(date)))
                .execute();

        entityManager.flush();
        entityManager.clear();

        log.info("[Batch:Update] 챌린지 참여 상태 업데이트 date = {}, toInProgress = {}, toEnd = {}",
                date, toInProgress, toEnd);
    }

    @Override
    public void updateWeeklyStatus(LocalDate date) {
        LocalDate monday = date.with(DayOfWeek.MONDAY);

        NumberExpression<Integer> eligibleDays =
                Expressions.numberTemplate(Integer.class,
                        "GREATEST(0, timestampdiff(DAY, GREATEST({0},{1}), LEAST({2},{3})) + 1)",
                        Expressions.constant(monday),
                        challengeParticipation.challenge.startDate,
                        Expressions.constant(date),
                        challengeParticipation.challenge.endDate
                );

        NumberExpression<Integer> effectiveRequired =
                Expressions.numberTemplate(Integer.class,
                        "CAST(function('ceil', (({0} * {1}) / 7.0)) AS int)",
                        challengeParticipation.challenge.rule, eligibleDays);

        BooleanExpression failExists =
                eligibleDays.gt(0)
                        .and(
                                JPAExpressions.selectOne()
                                        .from(routineStat)
                                        .where(
                                                routineStat.user.id.eq(challengeParticipation.user.id)
                                                        .and(routineStat.startDate.eq(monday))
                                                        .and(routineStat.activeDays.goe(effectiveRequired)))
                                        .notExists()
                        );

        long toFailed = jpaQueryFactory
                .update(challengeParticipation)
                .set(challengeParticipation.status, ParticipationStatus.FAILED)
                .set(challengeParticipation.updatedAt, LocalDateTime.now(clock))
                .where(isDeletedFalse()
                        .and(challengeParticipation.status.in(ParticipationStatus.INPROGRESS, ParticipationStatus.END))
                        .and(failExists))
                .execute();

        entityManager.flush();
        entityManager.clear();

        log.info("[Batch:Update] 주간 챌린지 참여 인증 상태 업데이트 date = {}, toFailed = {}", date, toFailed);
    }

    private BooleanBuilder booleanBuilder(String userId, ChallengeParticipationReadRequestDto requestDto) {

        return new BooleanBuilder()
                .and(isMine(userId))
                .and(isDeletedFalse())
                .and(challengeIsDeletedFalse())
                .and(eqStatus(requestDto.getStatus()))
                .and(betweenDate(requestDto.getDate()));
    }

    private BooleanExpression isMine(String userId) {
        return challengeParticipation.user.id.eq(userId);
    }

    private BooleanExpression isDeletedFalse() {
        return challengeParticipation.isDeleted.isFalse();
    }

    private BooleanExpression challengeIsDeletedFalse() {
        return challenge.isDeleted.isFalse();
    }

    private BooleanExpression eqStatus(ChallengeStatus status) {
        return status != null ? challenge.status.eq(status) : null;

    }
    private BooleanExpression betweenDate(LocalDate date) {
        if (date == null) return null;
        return challenge.startDate.loe(date)
                .and(challenge.endDate.goe(date));
    }
}
