package com.hae.library.controller;

import com.hae.library.jwt.JwtFilter;
import com.hae.library.jwt.TokenProvider;
import com.hae.library.dto.Jwt.TokenDto;
import com.hae.library.dto.Member.RequestLoginDto;
import com.hae.library.dto.ResponseResultDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/auth")
    public ResponseResultDto<Object> authorize(@RequestBody @Valid RequestLoginDto loginDto) {

        // 사용자가 로그인 정보를 전송하면, UsernamePasswordAuthenticationToken을 생성합니다.
        // 토큰은 사용자 이메일과 비밀번호를 포함합니다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                        loginDto.getPassword());

        // UsernamePasswordAuthenticationToken을 이용해서 사용자를 인증합니다.
        // 이때 사용자 정보는 'UserDetailsService'를 통해 DB에서 불러와집니다.
        // 비밀번호는 'PasswordEncoder'를 이용해서 암호화된 비밀번호와 비교됩니다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증된 사용자 정보는 SecurityContext에 저장됩니다.
        // 이를 통해 다른 곳에서도 사용자 정보를 참조할 수 있습니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증된 사용자 정보를 바탕으로 JWT를 생성합니다.
        TokenDto jwtDto = tokenProvider.createToken(authentication);

        // TODO: 필요하지 않을 거 같은데 확인 필요
        // JWT를 HTTP 응답 헤더에 포함시킵니다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtDto);

        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("로그인에 성공하였습니다")
                .data(jwtDto)
                .build();
    }
}

