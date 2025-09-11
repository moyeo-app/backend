package com.moyeo.backend.challenge.basic.infrastructure.repository;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.basic.application.mapper.ChallengeMapper;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.moyeo.backend.challenge.basic.domain.QChallenge.challenge;
import static com.moyeo.backend.common.util.QueryDslSortUtil.getOrderSpecifiers;

@Slf4j(topic = "CustomChallengeInfoRepository")
@RequiredArgsConstructor
public class CustomChallengeInfoRepositoryImpl implements CustomChallengeInfoRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    private final ChallengeMapper challengeMapper;
    private final Clock clock;

    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
            "title", challenge.title,
            "type", challenge.type,
            "status", challenge.status,
            "createdAt", challenge.createdAt
    );

    @Override
    public Page<ChallengeReadResponseDto> searchChallenges(ChallengeReadRequestDto requestDto, Pageable pageable) {

        BooleanBuilder booleanBuilder = booleanBuilder(requestDto);

        JPAQuery<Challenge> challenges = jpaQueryFactory
                .selectFrom(challenge)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS));

        JPAQuery<Long> total = jpaQueryFactory
                .select(challenge.count())
                .from(challenge)
                .where(booleanBuilder);

        List<ChallengeReadResponseDto> results = challenges.fetch().stream()
                .map(challengeMapper::toChallengeDto)
                .toList();

        return PageableExecutionUtils.getPage(
                results,
                pageable,
                total::fetchOne
        );
    }

    @Override
    public void updateStatus(LocalDate date) {

        BooleanExpression toInProgressBooleanBuilder = statusBooleanBuilder(date, ChallengeStatus.RECRUITING);

        long toInProgress = jpaQueryFactory
                .update(challenge)
                .set(challenge.status, ChallengeStatus.INPROGRESS)
                .set(challenge.updatedAt, LocalDateTime.now(clock))
                .where(toInProgressBooleanBuilder)
                .execute();

        BooleanExpression toEndBooleanBuilder = statusBooleanBuilder(date, ChallengeStatus.INPROGRESS);

        long toEnd = jpaQueryFactory
                .update(challenge)
                .set(challenge.status, ChallengeStatus.END)
                .set(challenge.updatedAt, LocalDateTime.now(clock))
                .where(toEndBooleanBuilder)
                .execute();

        entityManager.flush();
        entityManager.clear();

        log.info("[Batch:Update] 챌린지 상태 업데이트 date = {}, toInProgress = {}, toEnd = {}",
                date, toInProgress, toEnd);
    }

    private BooleanExpression statusBooleanBuilder(LocalDate date, ChallengeStatus status) {
        BooleanExpression base = isDeletedFalse().and(challenge.status.eq(status));

        if (status == ChallengeStatus.RECRUITING) {
            return base.and(challenge.startDate.loe(date));
        }
        if (status == ChallengeStatus.INPROGRESS) {
            return base.and(challenge.endDate.lt(date));
        }

        return Expressions.FALSE.isTrue();
    }

    private BooleanBuilder booleanBuilder(ChallengeReadRequestDto requestDto) {

        return new BooleanBuilder()
                .and(isDeletedFalse())
                .and(containsTitle(requestDto.getTitle()))
                .and(eqType(requestDto.getType()))
                .and(eqStatus(requestDto.getStatus()));
    }

    private BooleanExpression eqStatus(ChallengeStatus status) {
        return (status == null || status == ChallengeStatus.END)
                ? challenge.status.ne(ChallengeStatus.END)
                : challenge.status.eq(status);
    }

    private BooleanExpression eqType(ChallengeType type) {
        return type != null ? challenge.type.eq(type) : null;
    }

    private BooleanExpression containsTitle(String title) {
        return title != null ? challenge.title.contains(title) : null;
    }

    private BooleanExpression isDeletedFalse() {
        return challenge.isDeleted.isFalse();
    }

}
