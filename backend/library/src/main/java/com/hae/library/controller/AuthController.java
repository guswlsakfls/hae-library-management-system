package com.hae.library.controller;

import com.hae.library.JWT.JwtFilter;
import com.hae.library.JWT.TokenProvider;
import com.hae.library.dto.Jwt.TokenDto;
import com.hae.library.dto.Member.RequestLoginDto;
import com.hae.library.dto.Member.RequestMemberDto;
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
    public ResponseResultDto<Object> authorize(@Valid @RequestBody RequestLoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                        loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto jwtDto = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtDto);

//        return new ResponseEntity<>(jwtDto, httpHeaders, HttpStatus.OK);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("로그인에 성공하였습니다")
                .data(jwtDto)
                .build();
    }
}
