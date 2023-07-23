package com.hae.library.jwt;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// 유효한 자격증명을 제공하지 않고 접근하려 할때 401
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, java.io.IOException {
        log.info("JwtAuthenticationEntryPoint: {}, {}, {}", request, response, authException);
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
//        response.sendError(HttpServletResponse.
        response.sendRedirect("/error");
    }
}
