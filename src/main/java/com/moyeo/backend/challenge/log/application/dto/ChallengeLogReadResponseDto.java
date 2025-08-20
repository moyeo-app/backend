package com.moyeo.backend.challenge.log.application.dto;

import com.moyeo.backend.challenge.log.domain.ChallengeLogContent;
import com.moyeo.backend.challenge.log.domain.ChallengeLogStatus;
import com.moyeo.backend.challenge.log.domain.ContentLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "챌린지 인증 READ RESPONSE DTO")
public class ChallengeLogReadResponseDto {

    @Schema(description = "인증 ID", example = "ed8f52fe-f9c0-41db-9e52-57f25185c382")
    private String logId;

    @Schema(description = "닉네임", example = "코딩짱짱맨")
    private String nickname;

    @Schema(
            description = "인증 유형에 따른 내용",
            oneOf = {ContentLog.class}
    )
    private ChallengeLogContent content;

    @Schema(description = "챌린지 인증 상태", example = "SUCCESS")
    private ChallengeLogStatus status;
}
