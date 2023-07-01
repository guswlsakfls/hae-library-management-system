package com.hae.library.service;

import com.hae.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

//    public ResponseMemberDto signup(RequestSignupDto requestSignupDto) {
//        if (memberRepository.findByEmail(requestSignupDto.getEmail()).orElse(null) != null) {
//            throw new RestApiException(MemberErrorCode.MEMBER_ALREADY_EXIST);
//        }
//
//        Member member = Member.builder()
//                .email(requestSignupDto.getEmail())
//                .password(passwordEncoder.encode(requestSignupDto.getPassword()))
//                .role(Role.valueOf("ROLE_USER"))
//                .build();
//
//        return ResponseMemberDto.from(memberRepository.save(member));
//    }
}
