package com.moyeo.backend.auth.infrastructure.adapter;

import com.moyeo.backend.auth.domain.OAuthUserInfo;
import com.moyeo.backend.auth.infrastructure.dtos.GoogleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {

    private final GoogleResponseDto response;


    @Override
    public String getOauthId() {
        return response.getSub();
    }
}
