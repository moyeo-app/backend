package com.moyeo.backend.routine.application.mapper;

import com.moyeo.backend.routine.application.dto.RoutineStatReadResponseDto;
import com.moyeo.backend.routine.domain.RoutineStat;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RoutineStatMapper {

    RoutineStatReadResponseDto toRoutineStatDto(RoutineStat routineStat);
}
