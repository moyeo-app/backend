package com.moyeo.backend.routine.infrastructure.client;

import com.moyeo.backend.routine.domain.RoutineStat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

public abstract class AbstractAiClient implements AiClient {

    protected String settingPrompt(RoutineStat stat) {
        String prompt = """
            당신은 개인 학습 습관을 분석하고 동기를 부여하는 AI 코치입니다.
            주어진 학습 데이터를 바탕으로 분석, 격려, 그리고 구체적인 다음 주 계획을 제공해야 합니다.

            주간 총 공부 시간: %s (분)
            하루 평균: %s (분)
            집중 요일: %s
            적게 공부한 요일: %s
            출석률 높은 요일: %s
     
            아래 JSON 스키마로만 출력하세요. 각 필드는 **간결**, **중복 금지**:
            - routineAnalysis: 사실 요약만. 동일 문구 반복 금지.
            - emotionalFeedback: 격려 톤, 과장/나열/중복 금지.
            - nextWeekRoutine: 구체적 요일/목표 포함, 나열/중복 금지.
            """.formatted(
                    stat.getTotalMinutes(),
                    stat.getAvgMinutes(),
                    stat.getFocusDay(),
                    stat.getLeastDay(),
                    stat.getHighAttendanceDays().toString());

        return prompt;
    }

    protected HttpHeaders settingHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
