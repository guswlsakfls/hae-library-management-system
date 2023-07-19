package com.hae.library.config.Security;

import com.hae.library.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity  // 웹 보안을 활성화합니다.
@EnableMethodSecurity  // 메소드 수준에서의 보안을 활성화합니다.
@Configuration // 이 클래스를 스프링의 구성 클래스로 지정합니다.
public class SpringSecurityConfig {
    // JWT와 관련된 작업을 담당하는 클래스, 인증 오류 처리를 담당하는 클래스
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CorsFilter corsFilter;
    private final AuthenticationFailureHandler customAuthFailureHandler;

    public SpringSecurityConfig(TokenProvider tokenProvider,
                                JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                JwtAccessDeniedHandler jwtAccessDeniedHandler,
                                CorsFilter corsFilter,
                                AuthenticationFailureHandler customAuthFailureHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.corsFilter = corsFilter;
        this.customAuthFailureHandler = customAuthFailureHandler;
    }

    // 회원, 비회원, 관리자 모두 사용 가능합니다.
    private static final String[] ALL_URL_ARRAY = {
            /* 로그인, 회원가입 */
            "/api/signup",
            "/api/login",
            "/api/auth",
            "/api/category/all",
            "/manifest.json",

            /* 도서 */
            "/api/bookinfo/**",

            /* 기타 등등 */
            "/error",

    };

    // 회원, 관리자만 사용 가능 합니다.
    private static final String[] MEMBER_URL_ARRAY = {
            "/api/member/**",
    };

    // 관리자만 사용 가능 합니다.
    private static final String[] ADMIN_URL_ARRAY = {
            "/api/admin/**",
    };


    // 비밀번호를 암호화하는 역할을 담당하는 PasswordEncoder 빈을 생성합니다
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf(csrf -> csrf.disable())

                // corsFilter를 UsernamePasswordAuthenticationFilter 전에 추가합니다.
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // 예외 처리에 대한 설정을 합니다.
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // HTTP 요청에 대한 접근 제어를 설정합니다.
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(ALL_URL_ARRAY).permitAll()
                        .requestMatchers(MEMBER_URL_ARRAY).hasAnyRole("USER", "ADMIN")
                        .requestMatchers(ADMIN_URL_ARRAY).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // enable h2-console
                .headers(headers ->
                        headers.frameOptions(options ->
                                options.sameOrigin()
                        )
                )
                // JWT 보안 설정을 적용합니다.
                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();
    }
}
