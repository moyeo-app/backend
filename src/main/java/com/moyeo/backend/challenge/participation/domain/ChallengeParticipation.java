package com.moyeo.backend.challenge.participation.domain;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.participation.domain.enums.ParticipationStatus;
import com.moyeo.backend.common.domain.BaseEntity;
import com.moyeo.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "challenge_participation")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeParticipation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(value = EnumType.STRING)
    private ParticipationStatus status;
}
