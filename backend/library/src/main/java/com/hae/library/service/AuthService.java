package com.hae.library.service;

import com.hae.library.JWT.TokenProvider;
import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Member;
import com.hae.library.dto.Jwt.TokenDto;
import com.hae.library.dto.Member.RequestMemberDto;
import com.hae.library.dto.Member.ResponseMemberDto;
import com.hae.library.global.Exception.ErrorCode;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public ResponseMemberDto signup(RequestMemberDto requestMemberDto) {
        if (memberRepository.findByEmail(requestMemberDto.getEmail()).orElse(null) != null) {
            throw new RestApiException(MemberErrorCode.MEMBER_ALREADY_EXIST);
        }

        Member member = Member.builder()
                .name(requestMemberDto.getName())
                .email(requestMemberDto.getEmail())
                .password(passwordEncoder.encode(requestMemberDto.getPassword()))
                .role(Role.valueOf("ROLE_USER"))
                .build();

        return ResponseMemberDto.from(memberRepository.save(member));
    }
}
