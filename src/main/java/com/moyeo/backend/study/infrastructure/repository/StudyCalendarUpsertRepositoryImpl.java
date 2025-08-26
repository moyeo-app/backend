package com.moyeo.backend.study.infrastructure.repository;

import com.moyeo.backend.challenge.log.application.dto.ChallengeLogDailyAggregateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class StudyCalendarUpsertRepositoryImpl implements StudyCalendarUpsertRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AuditorAware<String> auditorAware;

    private static final int BATCH_SIZE = 500;
    private static final String SQL = """
        INSERT INTO study_calendar (
            id,
            user_id, date, total_minutes,
            created_at, created_by,
            updated_at, updated_by,
            is_deleted
        )
        VALUES (
            :id,
            :userId, :date, :totalMinutes,
            :now,    :actor,
            :now,    :actor,
            false
        )
        ON CONFLICT (user_id, date)
        DO UPDATE SET
            total_minutes = EXCLUDED.total_minutes,
            updated_at    = EXCLUDED.updated_at,
            updated_by    = EXCLUDED.updated_by
        """;

    @Override
    public void upsertAll(List<ChallengeLogDailyAggregateDto> list) {
        final String actor = auditorAware.getCurrentAuditor().orElse("SYSTEM");
        final LocalDateTime now =  LocalDateTime.now(ZoneId.of("Asia/Seoul"));

         List<SqlParameterSource> buffer = new ArrayList<>(BATCH_SIZE);

         for (ChallengeLogDailyAggregateDto c : list) {
             buffer.add(new MapSqlParameterSource()
                     .addValue("id", UUID.randomUUID().toString())
                     .addValue("userId", c.userId())
                     .addValue("date", c.date())
                     .addValue("totalMinutes", c.totalMinutes())
                     .addValue("actor",  actor)
                     .addValue("now",    now));

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
