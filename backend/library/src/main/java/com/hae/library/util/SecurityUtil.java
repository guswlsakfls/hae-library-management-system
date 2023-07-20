package com.hae.library.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    private SecurityUtil() { }

    /**
     * 현재 인증된 회원의 ID를 반환합니다.
     *
     * @return 현재 인증된 회원의 ID
     * @throws RuntimeException Security Context에 인증 정보가 없는 경우 발생하는 예외
     */
    public static String getCurrentMemberEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("authentication.getName(): {}", authentication.getName());

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }

        return authentication.getName();
    }
}
