package com.hae.library.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// HTTP 요청이 들어올 때마다 JWT 토큰이 유효한지를 검사하는 역할을 하는 필터입니다.
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    @Override
    public void configure(HttpSecurity http) {
        // JwtFilter 객체를 생성합니다.
        JwtFilter customFilter = new JwtFilter(tokenProvider);

        // 생성한 필터를 UsernamePasswordAuthenticationFilter 전에 추가합니다.
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

        log.debug("JwtFilter가 HttpSecurity에 추가되었습니다.");
    }
}
