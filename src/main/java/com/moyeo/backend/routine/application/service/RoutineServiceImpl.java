package com.moyeo.backend.routine.application.service;

import com.moyeo.backend.auth.application.service.UserContextService;
import com.moyeo.backend.routine.application.dto.RoutineStatReadRequestDto;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;
import com.moyeo.backend.routine.application.mapper.RoutineStatMapper;
import com.moyeo.backend.routine.domain.RoutineStat;
import com.moyeo.backend.routine.domain.RoutineStatRepository;
import com.moyeo.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Slf4j(topic = "RoutineStatService")
@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {

    private final UserContextService userContextService;
    private final RoutineStatRepository routineStatRepository;
    private final RoutineStatMapper routineStatMapper;

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
}
