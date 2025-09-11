package com.moyeo.backend.routine.infrastructure.client;

import com.moyeo.backend.routine.domain.Report;
import com.moyeo.backend.routine.domain.RoutineStat;

public interface AiClient {
    Report generateResponse(RoutineStat stat);
}
