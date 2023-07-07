package com.hae.library.controller;

import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.jwt.JwtFilter;
import com.hae.library.jwt.TokenProvider;
import com.hae.library.dto.Jwt.TokenDto;
import com.hae.library.dto.Member.RequestLoginDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.repository.MemberRepository;
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
    private final MemberRepository memberRepo;

    @PostMapping("/auth")
    public ResponseResultDto<Object> authorize(@RequestBody @Valid RequestLoginDto loginDto) {

        // 사용자의 계정이 휴면 상태인지 확인합니다. // TODO: 403 에러를 뱉는다. 추우 확인 필요
        memberRepo.findByEmail(loginDto.getEmail()).ifPresent(member -> {
            if (!member.isActivated()) {
                throw new RestApiException(MemberErrorCode.MEMBER_DISABLED);
            }
        });

        // UsernamePasswordAuthenticationToken 생성합니다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        // 사용자 인증을 합니다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 SecurityContext에 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증된 사용자를 위한 JWT 토큰 생성합니다.
        TokenDto jwtDto = tokenProvider.createToken(authentication);

        // 응답 헤더에 JWT를 포함합니다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtDto);

        // 결과를 반환합니다.
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("로그인에 성공하였습니다")
                .data(jwtDto)
                .build();
    }
}

