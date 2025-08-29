package com.moyeo.backend.routine.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.routine.application.dto.RoutineStatReadRequestDto;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;
import com.moyeo.backend.routine.application.mapper.RoutineStatMapper;
import com.moyeo.backend.routine.domain.*;
import com.moyeo.backend.routine.infrastructure.client.AiClient;
import com.moyeo.backend.study.application.dto.WeeklyAgg;
import com.moyeo.backend.study.domain.StudyCalendarRepository;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Slf4j(topic = "RoutineStatService")
@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {

    private final UserContextService userContextService;
    private final RoutineStatRepository routineStatRepository;
    private final RoutineStatMapper routineStatMapper;
    private final StudyCalendarRepository studyCalendarRepository;
    private final AiClient aiClient;
    private final RoutineReportRepository routineReportRepository;

    @Override
    @Transactional
    public void upsertWeeklyStat(LocalDate weekEndDate) {
        LocalDate monday = weekEndDate.with(DayOfWeek.MONDAY);

        List<WeeklyAgg> list = studyCalendarRepository.findWeeklyAgg(monday, weekEndDate);
        if (list.isEmpty()) return;

        List<RoutineStatReadResponseDto> stats = list.stream()
                .map(this::computeWeeklyAgg)
                .toList();

        routineStatRepository.upsertAll(stats);
        log.info("주간 학습 통계 업데이트 완료, weekStart = {}, stats = {}", monday, stats);
    }

    @Override
    @Transactional(readOnly = true)
    public RoutineStatReadResponseDto getRoutineStat(RoutineStatReadRequestDto requestDto) {
        User currentUser = userContextService.getCurrentUser();
        String userId = currentUser.getId();

        LocalDate startDate = requestDto.getDate() == null ?
                LocalDate.now(ZoneId.of("Asia/Seoul")).with(previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1)
                : requestDto.getDate().with(previousOrSame(DayOfWeek.MONDAY));

        RoutineStat routineStat = routineStatRepository.findByUserIdAndStartDateAndIsDeletedFalse(userId, startDate)
                .orElse(null);

        return routineStatMapper.toRoutineStatDto(routineStat);
    }

    @Override
    @Transactional
    public void upsertWeeklyReport(LocalDate weekEndDate) {

        LocalDate monday = weekEndDate.with(DayOfWeek.MONDAY);
        List<RoutineStat> list = routineStatRepository.findAllByStartDateAndIsDeletedFalse(monday);
        if (list.isEmpty()) return;

        ArrayList<RoutineReport> reports = new ArrayList<>(list.size());
        for (RoutineStat stat : list) {
            try {
                Report response = aiClient.generateResponse(stat);
                RoutineReport report = RoutineReport.builder()
                        .user(stat.getUser())
                        .startDate(stat.getStartDate())
                        .provider("GEMINI")
                        .model("gemini-2.5-flash")
                        .report(response)
                        .build();
                reports.add(report);
            } catch (Exception e) {
                log.warn("루틴 리포트 생성 실패, userId = {}, weekStart = {}",
                        stat.getUser().getId(), monday, e);
            }

            if (!reports.isEmpty()) {
                routineReportRepository.upsertAll(reports);
                log.info("루틴 리포트 업데이트 완료: {} 건 (실패 {} 건), weekStart = {}",
                        reports.size(), list.size() - reports.size(), monday);
            }
        }
    }

    private RoutineStatReadResponseDto computeWeeklyAgg(WeeklyAgg agg) {
        int[] byDay = new int[]{ agg.mon(), agg.tue(), agg.wed(), agg.thu(), agg.fri(), agg.sat(), agg.sun() };
        int maxIdx = IntStream.range(1, 7)
                .reduce(0, (best, i) -> byDay[i] > byDay[best] ? i : best);
        int minIdx = IntStream.range(1, 7)
                .reduce(0, (best, i) -> byDay[i] < byDay[best] ? i : best);

        DayOfWeek focusDay = DayOfWeek.values()[maxIdx];
        DayOfWeek leastDay = DayOfWeek.values()[minIdx];
        List<DayOfWeek> highAttendanceDays = IntStream.range(0, 7)
                .boxed()
                .filter(i -> byDay[i] > 0)
                .sorted((a, b) -> {
                    int cmp = Integer.compare(byDay[b], byDay[a]);
                    return (cmp != 0) ? cmp : Integer.compare(a, b);
                })
                .limit(3)
                .map(i -> DayOfWeek.values()[i])
                .toList();

        return RoutineStatReadResponseDto.builder()
                .userId(agg.userId())
                .startDate(agg.startDate())
                .totalMinutes(agg.totalMinutes())
                .avgMinutes(agg.avgMinutes())
                .focusDay(focusDay)
                .leastDay(leastDay)
                .highAttendanceDays(highAttendanceDays)
                .build();
    }
}
