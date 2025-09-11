package com.moyeo.backend.routine.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeo.backend.routine.domain.RoutineReport;
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

@Slf4j(topic = "RoutineReportUpsertRepositoryImpl")
@RequiredArgsConstructor
public class RoutineReportUpsertRepositoryImpl implements RoutineReportUpsertRepository {
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AuditorAware<String> auditorAware;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    private static final int BATCH_SIZE = 500;
    private static final String SQL = """
        INSERT INTO routine_report (
            id,
            user_id, start_date,
            provider, model, report,
            created_at, created_by, updated_at, updated_by, is_deleted
        )
        VALUES (
            :id,
            :userId, :startDate,
            :provider, :model, CAST(:report AS jsonb),
            :now, :actor, :now, :actor, false
        )
        ON CONFLICT (user_id, start_date)
        DO UPDATE SET
            provider = EXCLUDED.provider,
            model   = EXCLUDED.model,
            report     = EXCLUDED.report,
            updated_at    = EXCLUDED.updated_at,
            updated_by    = EXCLUDED.updated_by,
            is_deleted    = false
        """;
    
    @Override
    public void upsertAll(List<RoutineReport> reports) {
        final String actor = auditorAware.getCurrentAuditor().orElse("SYSTEM");
        final LocalDateTime now =  LocalDateTime.now(clock);

        List<SqlParameterSource> buffer = new ArrayList<>(BATCH_SIZE);
        for (RoutineReport r : reports) {
            String reportJson;
            try {
                reportJson = objectMapper.writeValueAsString(r.getReport());
            } catch (Exception e) {
                log.warn("Report 직렬화 실패, userId={}, startDate={}", r.getUser().getId(), r.getStartDate(), e);
                continue;
            }
            buffer.add(new MapSqlParameterSource()
                    .addValue("id", UUID.randomUUID())
                    .addValue("userId", r.getUser().getId())
                    .addValue("startDate", r.getStartDate())
                    .addValue("provider", r.getProvider())
                    .addValue("model", r.getModel())
                    .addValue("report", reportJson)
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
