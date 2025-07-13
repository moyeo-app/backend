package com.moyeo.backend.challenge.basic.presentation;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeOptionDto;
import com.moyeo.backend.challenge.basic.application.dto.StartEndOptionDto;
import com.moyeo.backend.challenge.basic.application.dto.TimeOptionDto;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.TimeOption;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "OptionDeserializer")
@Component
@RequiredArgsConstructor
public class OptionDeserializer extends JsonDeserializer<ChallengeOptionDto> {

    private final ObjectMapper mapper;

    @Override
    public ChallengeOptionDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        log.info("option : {}", node);
        return getOption(node);
    }

    private ChallengeOptionDto getOption(JsonNode node) throws JsonProcessingException {
        if (node.has("time")) {
            // time 만 있으면 TIME 타입으로 간주
            return mapper.treeToValue(node, TimeOptionDto.class);
        } else if (node.has("start") && node.has("end")) {
            // start, end 가 있으면 ATTENDANCE 또는 CONTENT 타입으로 간주
            return mapper.treeToValue(node, StartEndOptionDto.class);
        } else {
            throw new CustomException(ErrorCode.INVALID_OPTION_FORMAT);
        }
    }
}
