package com.moyeo.backend.routine.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.routine.domain.Report;
import com.moyeo.backend.routine.domain.RoutineStat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j(topic = "GeminiClient")
@Component
@RequiredArgsConstructor
public class GeminiClient extends AbstractAiClient {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${ai.gemini.base-url}")
    private String baseUrl;

    @Value("${ai.gemini.model}")
    private String model;

    @Value("${ai.gemini.key}")
    private String apiKey;

    @Override
    public Report generateResponse(RoutineStat stat) {
        String URL = baseUrl + "/v1beta/models/" + model + ":generateContent";;
        HttpHeaders headers = settingHeader();
        headers.set("x-goog-api-key", apiKey);

        String prompt = settingPrompt(stat);
        ObjectNode body = buildRequestBody(prompt);

        HttpEntity<ObjectNode> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    URL,
                    entity,
                    String.class
            );
            if (response.getBody() == null) {
                log.error("Gemini 응답 비어있음");
                throw new CustomException(ErrorCode.AI_BAD_RESPONSE);
            }
            log.info("Gemini 전체 응답: {}", response.getBody());
            JsonNode node = objectMapper.readTree(response.getBody());

            String json = node.path("candidates")
                    .path(0).path("content").path("parts")
                    .path(0).path("text").asText();

            if (json == null || json.isBlank()) {
                log.error("Gemini 응답 candidates 비어있음");
                throw new CustomException(ErrorCode.AI_BAD_RESPONSE);
            }

            JsonNode root = objectMapper.readTree(json);
            if (root.path("routineAnalysis").isMissingNode()
                    || root.path("emotionalFeedback").isMissingNode()
                    || root.path("nextWeekRoutine").isMissingNode()) {
                throw new CustomException(ErrorCode.AI_BAD_RESPONSE);
            }
            return objectMapper.treeToValue(objectMapper.readTree(json), Report.class);

        } catch (HttpClientErrorException e) {
            log.error("Gemini API 요청 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.AI_API_CALL_FAILED);
        } catch (Exception e) {
            log.error("Gemini API 응답 처리 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.AI_API_CALL_FAILED);
        }
    }

    private ObjectNode buildRequestBody(String prompt) {
        // contents
        ArrayNode parts = objectMapper.createArrayNode()
                .add(objectMapper.createObjectNode().put("text", prompt));
        ObjectNode content = objectMapper.createObjectNode();
        content.put("role", "user");
        content.set("parts", parts);
        ArrayNode contents = objectMapper.createArrayNode().add(content);

        // responseSchema
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "OBJECT");
        ObjectNode props = objectMapper.createObjectNode();
        ObjectNode stringType = objectMapper.createObjectNode()
                .put("type", "STRING")
                .put("maxLength", 300);
        props.set("routineAnalysis", stringType);
        props.set("emotionalFeedback", stringType);
        props.set("nextWeekRoutine", stringType);
        schema.set("properties", props);

        // thinkingConfig
        ObjectNode thinkingConfig = objectMapper.createObjectNode();
        thinkingConfig.put("thinkingBudget", 0);

        // generationConfig
        ObjectNode generationConfig = objectMapper.createObjectNode();
        generationConfig.put("responseMimeType", "application/json");
        generationConfig.set("responseSchema", schema);
        generationConfig.put("temperature", 0.2);
        generationConfig.put("topK", 1);
        generationConfig.put("topP", 0.6);
        generationConfig.put("maxOutputTokens", 1024);
        generationConfig.set("thinkingConfig", thinkingConfig);

        ObjectNode body = objectMapper.createObjectNode();
        body.set("contents", contents);
        body.set("generationConfig", generationConfig);
        return body;
    }
}
