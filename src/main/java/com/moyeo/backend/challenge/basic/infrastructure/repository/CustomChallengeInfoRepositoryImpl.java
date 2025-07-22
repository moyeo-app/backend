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
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.moyeo.backend.challenge.basic.domain.QChallenge.challenge;
import static com.moyeo.backend.common.util.QueryDslSortUtil.getOrderSpecifiers;

@RequiredArgsConstructor
public class CustomChallengeInfoRepositoryImpl implements CustomChallengeInfoRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ChallengeMapper challengeMapper;

    private static final Map<String, ComparableExpressionBase<?>> SORT_PARAMS = Map.of(
            "title", challenge.title,
            "type", challenge.type,
            "status", challenge.status,
            "createdAt", challenge.createdAt
    );

    @Override
    public Page<ChallengeReadResponseDto> searchChallenges(ChallengeReadRequestDto requestDto, Pageable pageable) {

        List<Challenge> challenges = jpaQueryFactory
                .selectFrom(challenge)
                .where(booleanBuilder(requestDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable, SORT_PARAMS))
                .fetch();

        List<ChallengeReadResponseDto> results = challenges.stream()
                .map(challengeMapper::toChallengeDto)
                .toList();

        Long total = Objects.requireNonNullElse(
                jpaQueryFactory
                        .select(challenge.count())
                        .from(challenge)
                        .fetchOne(),
                0L
        );
        return new PageImpl<>(results, pageable, total);
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
