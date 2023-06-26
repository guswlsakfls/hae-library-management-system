package com.hae.library.service;

import com.hae.library.util.SecurityUtil;
import com.hae.library.domain.Member;
import com.hae.library.dto.Member.ResponseMemberDto;
import com.hae.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseMemberDto getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(ResponseMemberDto::from)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public ResponseMemberDto changeMemberName(String email, String name) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        member.updateName(name);
        return ResponseMemberDto.from(memberRepository.save(member));
    }

    @Transactional
    public ResponseMemberDto changeMemberPassword(String email, String exPassword, String newPassword) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, member.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        member.updatePassword(passwordEncoder.encode((newPassword)));
        return ResponseMemberDto.from(memberRepository.save(member));
    }
}
