package com.moyeo.backend.auth.application.dto;

import com.moyeo.backend.user.domain.Bank;
import com.moyeo.backend.user.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "소셜 로그인 RESPONSE DTO")
public class LoginResponseDto {
    @Schema(description = "신규 사용자 유무 (true 면 회원 등록 필요)", example = "true")
    private boolean isNewUser;

    @Schema(description = "JWT Access Token (기존 사용자일 경우 응답)", example = "null")
    private String jwtAccessToken;

    @Schema(description = "소셜 로그인 고유 사용자 ID (신규 회원일 경우 응답)", example = "1122385832637846")
    private String oauthId;

    @Schema(description = "사용자 ID", example = "d8f52fe-f9c0-41db-9e52-57f25185c382")
    private String userId;

    @Schema(description = "닉네임", example = "코딩짱짱맨")
    private String nickname;

    @Schema(description = "캐릭터 (BEAR, RABBIT, CAT, PIG)", example = "BEAR")
    private Character character;

    @Schema(description = "은행 (KB, SHINHAN, WOORI, NH, HANA, TOSS, KAKAO)", example = "KB")
    private Bank bank;

    @Schema(description = "계좌 번호", example = "812702-02-442698")
    private String accountNumber;
}
