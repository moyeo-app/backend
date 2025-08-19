package com.moyeo.backend.challenge.log.domain;

import com.moyeo.backend.challenge.basic.domain.Challenge;
import com.moyeo.backend.challenge.participation.domain.ChallengeParticipation;
import com.moyeo.backend.common.domain.BaseEntity;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

@Entity
@Table(name = "challenge_log",
        uniqueConstraints = @UniqueConstraint(columnNames = {"challenge_id", "participation_id", "date"}))
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "participation_id", nullable = false)
    private ChallengeParticipation participation;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "jsonb")
    @Type(JsonType.class)
    private ChallengeLogContent content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeLogStatus status;

    public void updateContent(String text) {
        if (!(content instanceof ContentLog contentLog)) {
            throw new CustomException(ErrorCode.CHALLENGE_LOG_CONTENT_TYPE_MISMATCH);
        }

        this.content = ContentLog.builder()
                .keywords(contentLog.getKeywords())
                .text(text)
                .build();
    }
}
