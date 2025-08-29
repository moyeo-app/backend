package com.moyeo.backend.routine.application.mapper;

import com.moyeo.backend.routine.application.dto.RoutineReportReadResponseDto;
import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;
import com.moyeo.backend.routine.domain.RoutineReport;
import com.moyeo.backend.routine.domain.RoutineStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RoutineMapper {

    @Mapping(source = "user.id", target = "userId")
    RoutineStatReadResponseDto toRoutineStatDto(RoutineStat routineStat);

    @Mapping(source = "user.id", target = "userId")
    RoutineReportReadResponseDto toRoutineReportDto(RoutineReport routineReport);
}
