package com.moyeo.backend.routine.domain;

import com.moyeo.backend.common.domain.BaseEntity;
import com.moyeo.backend.user.domain.User;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDate;

@Entity
@Table(name = "routine_report",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "start_date"}))
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, columnDefinition = "jsonb")
    @Type(JsonType.class)
    private Report report;
}
