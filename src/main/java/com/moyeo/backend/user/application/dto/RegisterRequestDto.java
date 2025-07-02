package com.moyeo.backend.user.application.dto;

import com.moyeo.backend.auth.domain.Provider;
import com.moyeo.backend.user.domain.Bank;
import com.moyeo.backend.user.domain.Character;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "사용자 등록 REQUEST DTO")
@Builder
public class RegisterRequestDto {
    @Schema(description = "소셜 로그인 종류 (KAKAO, GOOGLE)", example = "KAKAO")
    @NotNull
    private Provider provider;

    @Schema(description = "소셜 로그인 고유 사용자 ID", example = "1122385832637846")
    @NotBlank
    private String oauthId;

    @Schema(description = "닉네임", example = "코딩짱짱맨")
    @NotBlank
    private String nickname;

    @Schema(description = "캐릭터 (BEAR, RABBIT, CAT, PIG)", example = "BEAR")
    @NotNull
    private Character character;

    @Schema(description = "은행 (KB, SHINHAN, WOORI, NH, HANA, TOSS, KAKAO)", example = "KB")
    @NotNull
    private Bank bank;

    @Schema(description = "계좌 번호", example = "812702-02-442698")
    @NotNull
    private String accountNumber;
}
