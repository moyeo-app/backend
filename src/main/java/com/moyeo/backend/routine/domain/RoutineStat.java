package com.moyeo.backend.routine.domain;

import com.moyeo.backend.common.domain.BaseEntity;
import com.moyeo.backend.user.domain.User;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "routine_stat",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "start_date"}))
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineStat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private int totalMinutes;

    @Column(nullable = false)
    private int avgMinutes;

    @Column(nullable = false)
    private int activeDays;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek focusDay;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek leastDay;

    @Column(nullable = false, columnDefinition = "jsonb")
    @Type(JsonType.class)
    private List<DayOfWeek> highAttendanceDays;
}
