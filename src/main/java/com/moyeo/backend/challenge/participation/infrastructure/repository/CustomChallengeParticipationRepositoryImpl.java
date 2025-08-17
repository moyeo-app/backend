package com.moyeo.backend.challenge.participation.infrastructure.repository;

import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadRequestDto;
import com.moyeo.backend.challenge.participation.application.dto.ChallengeParticipationReadResponseDto;
import com.moyeo.backend.challenge.participation.application.mapper.ChallengeParticipationMapper;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.moyeo.backend.challenge.basic.domain.QChallenge.challenge;
import static com.moyeo.backend.challenge.participation.domain.QChallengeParticipation.challengeParticipation;
import static com.moyeo.backend.common.util.QueryDslSortUtil.getOrderSpecifiers;

@RequiredArgsConstructor
public class CustomChallengeParticipationRepositoryImpl implements CustomChallengeParticipationRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final ChallengeParticipationMapper participationMapper;

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
