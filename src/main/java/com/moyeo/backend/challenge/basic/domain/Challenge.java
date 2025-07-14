package com.moyeo.backend.challenge.basic.domain;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeOptionDto;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeStatus;
import com.moyeo.backend.challenge.basic.domain.enums.ChallengeType;
import com.moyeo.backend.common.domain.BaseEntity;
import com.moyeo.backend.user.domain.User;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

@Entity
@Table(name = "challenge_info")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeType type;

    @Column(nullable = false)
    private int maxParticipants;

    @Column(nullable = false)
    private int participantsCount;

    @Column(nullable = false)
    private int fee;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeStatus status;

    @Column(nullable = false, columnDefinition = "jsonb")
    @Type(JsonType.class)
    private ChallengeOption option;

    @Column(nullable = false)
    private int rule;
}
