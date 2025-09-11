package com.moyeo.backend.routine.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j(topic = "RoutineStatUpsertRepositoryImpl")
@RequiredArgsConstructor
public class RoutineStatUpsertRepositoryImpl implements RoutineStatUpsertRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AuditorAware<String> auditorAware;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    private static final int BATCH_SIZE = 500;
    private static final String SQL = """
        INSERT INTO routine_stat (
            id,
            user_id, start_date,
            total_minutes, avg_minutes, active_days,
            focus_day, least_day, high_attendance_days,
            created_at, created_by, updated_at, updated_by, is_deleted
        )
        VALUES (
            :id,
            :userId, :startDate,
            :totalMinutes, :avgMinutes, :activeDays,
            :focusDay, :leastDay, CAST(:highAttendanceJson AS jsonb),
            :now, :actor, :now, :actor, false
        )
        ON CONFLICT (user_id, start_date)
        DO UPDATE SET
            total_minutes = EXCLUDED.total_minutes,
            avg_minutes   = EXCLUDED.avg_minutes,
            active_days   = EXCLUDED.active_days,
            focus_day     = EXCLUDED.focus_day,
            least_day     = EXCLUDED.least_day,
            high_attendance_days = EXCLUDED.high_attendance_days,
            updated_at    = EXCLUDED.updated_at,
            updated_by    = EXCLUDED.updated_by,
            is_deleted    = false
        """;

    @Override
    public void upsertAll(List<RoutineStatReadResponseDto> stats) {
        final String actor = auditorAware.getCurrentAuditor().orElse("SYSTEM");
        final LocalDateTime now =  LocalDateTime.now(clock);

        List<SqlParameterSource> buffer = new ArrayList<>(BATCH_SIZE);

        for (RoutineStatReadResponseDto r : stats) {
            String highJson;
            try {
                highJson = objectMapper.writeValueAsString(
                        r.highAttendanceDays().stream().map(Enum::name).toList()
                );
            } catch (Exception e) {
                highJson = "[]";
                log.warn("출석률 높은 요일 직렬화 실패, userId={}, startDate={}, e=[]", r.userId(), r.startDate(), e);
            }

            buffer.add(new MapSqlParameterSource()
                    .addValue("id", UUID.randomUUID())
                    .addValue("userId", r.userId())
                    .addValue("startDate", r.startDate())
                    .addValue("totalMinutes", r.totalMinutes())
                    .addValue("avgMinutes", r.avgMinutes())
                    .addValue("activeDays", r.activeDays())
                    .addValue("focusDay", r.focusDay().name())
                    .addValue("leastDay", r.leastDay().name())
                    .addValue("highAttendanceJson", highJson)
                    .addValue("actor", actor)
                    .addValue("now", now)
            );

            if (buffer.size() == BATCH_SIZE) {
                jdbcTemplate.batchUpdate(SQL, buffer.toArray(SqlParameterSource[]::new));
                buffer.clear();
            }
        }
        if (!buffer.isEmpty()) {
            jdbcTemplate.batchUpdate(SQL, buffer.toArray(SqlParameterSource[]::new));
        }
    }
}
