package com.moyeo.backend.auth.infrastructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "JwtAuthenticationEntryPoint")
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.warn("인증 실패: {}", authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Object> errorResponse = ApiResponse.fail(ErrorCode.MISSING_TOKEN);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
