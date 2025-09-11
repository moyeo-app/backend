package com.moyeo.backend.auth.infrastructure.adapter;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.infrastructure.dtos.KakaoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class KakaoUserInfo implements OAuthUserInfo {

    private final KakaoResponseDto response;

    @Override
    public String getOauthId() {
        return response.getId().toString();
    }
}
