package com.hae.library.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

// JWT를 위한 커스텀 필터를 만듭니다.
@Slf4j
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // 실제 필터링 작업을 수행하는 메서드입니다.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        // HTTP 요청에서 토큰 정보를 가져옵니다.
        String jwt = resolveToken(httpServletRequest);

        // 요청 URI를 가져옵니다.
        String requestURI = httpServletRequest.getRequestURI();

        log.debug("해당 토큰이 유효한지 확인 절차 - requestURI: {}", requestURI);
        // 토큰이 존재하며 해당 토큰이 유효하다면
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

            // 토큰에서 인증 정보를 가져와서 SecurityContext에 저장합니다.
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        // 다음 필터 체인을 실행합니다.
        filterChain.doFilter(servletRequest, servletResponse);
    }

    // HTTP 요청 헤더에서 토큰 정보를 추출하는 메서드입니다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        // Bearer 토큰이 존재한다면, 토큰 문자열만 추출하여 반환합니다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
