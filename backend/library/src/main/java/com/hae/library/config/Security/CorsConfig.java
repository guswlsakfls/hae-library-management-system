package com.hae.library.config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // URL 기반 CORS 구성을 위한 소스를 생성합니다.
        CorsConfiguration config = new CorsConfiguration(); // CORS 구성을 생성합니다.
        config.setAllowCredentials(true); // 쿠키와 같은 자격 증명 정보를 요청에 포함시킬지 여부를 설정합니다. 여기서는 true로 설정하여 자격 증명을 허용합니다.
        config.addAllowedOriginPattern("*"); // 요청을 허용할 출처를 설정합니다. 여기서는 "*"을 사용하여 모든 출처를 허용합니다.
        config.addAllowedHeader("*"); // 요청을 허용할 헤더를 설정합니다. 여기서는 "*"을 사용하여 모든 헤더를 허용합니다.
        config.addAllowedMethod("*"); // 요청을 허용할 HTTP 메서드를 설정합니다. 여기서는 "*"을 사용하여 모든 메서드를 허용합니다.

        source.registerCorsConfiguration("/api/**", config); // "/api/**" 경로로 들어오는 모든 요청에 대해 위에서 설정한 CORS 구성을 적용합니다.
        return new CorsFilter(source); // 위에서 생성한 source를 사용하여 새 CorsFilter를 생성하고 반환합니다.
    }
}
