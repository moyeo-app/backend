package com.moyeo.backend.auth.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyeo.backend.auth.infrastructure.security.CustomUserDetails;
import com.moyeo.backend.auth.util.JwtUtil;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import com.moyeo.backend.common.response.ApiResponse;
import com.moyeo.backend.user.domain.User;
import com.moyeo.backend.user.domain.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JwtAuthenticationFilter")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getJwtFromHeader(request);
        log.info("요청 URI: {}", request.getRequestURI());
        log.info("JWT 토큰 입력 : {}", token);

        if (StringUtils.hasText(token)) {
            if (jwtUtil.validateToken(token)) {
                Claims info = jwtUtil.getUserInfoFromToken(token);
                setAuthentication(info.getSubject());
            } else {
                log.warn("JWT 토큰 오류");
                setErrorResponse(response, ErrorCode.INVALID_TOKEN);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String userId) {
        Authentication authentication = createAuthentication(userId);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(String userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserDetails userDetails = CustomUserDetails.builder()
                .user(user)
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(ApiResponse.fail(errorCode))
        );
    }
}
