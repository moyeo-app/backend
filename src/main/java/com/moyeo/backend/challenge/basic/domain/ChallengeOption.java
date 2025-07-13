package com.moyeo.backend.challenge.basic.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TimeOption.class, name = "TIME"),
        @JsonSubTypes.Type(value = StartEndOption.class, name = "START_END")
})
public abstract class ChallengeOption {
}
