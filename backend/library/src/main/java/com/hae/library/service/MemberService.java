package com.hae.library.service;

import com.hae.library.domain.Member;
import com.hae.library.dto.Member.LoginRequestDto;
import com.hae.library.dto.Member.SignupRequestDto;
import com.hae.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepo;

    @Transactional
    public Member signup(SignupRequestDto signupRequestDto) {
        // 회원 가입 로직 구현
        Member member = Member.builder()
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .password(signupRequestDto.getPassword())
                .build();
        return memberRepo.save(member);
    }

    // TODO: accessToken 발행 되야 하지 않나?
    public boolean login(LoginRequestDto loginRequestDto) {
        // 로그인 로직 구현
        Optional<Member> optionalMember = memberRepo.findByEmail(loginRequestDto.getEmail());
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (member.getPassword().equals(loginRequestDto.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public Member getMemberById(Long memberId) {
        // 특정 회원 조회 로직 구현
        return memberRepo.findById(memberId).orElse(null);
    }

    @Transactional
    public void updateMemberPassword(Long memberId, String newPassword) {
        // 회원 비밀번호 수정 로직 구현
        Optional<Member> optionalMember = memberRepo.findById(memberId);
        optionalMember.ifPresent(member -> {
            member.setPassword(newPassword);
            memberRepo.save(member);
        });
    }

    @Transactional
    public void updateMemberName(Long memberId, String newName) {
        // 회원 이름 수정 로직 구현
        Optional<Member> optionalMember = memberRepo.findById(memberId);
        optionalMember.ifPresent(member -> {
            member.setName(newName);
            memberRepo.save(member);
        });
    }

    @Transactional
    public boolean deleteMember(Long memberId) {
        // 회원 삭제 로직 구현
        Optional<Member> optionalMember = memberRepo.findById(memberId);
        if (optionalMember.isPresent()) {
            memberRepo.deleteById(memberId);
            return true;
        }
        return false;
    }
}
