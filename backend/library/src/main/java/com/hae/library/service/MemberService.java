package com.hae.library.service;

import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.Member.*;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.util.SecurityUtil;
import com.hae.library.domain.Member;
import com.hae.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<ResponseMemberDto> getAllMember() {
        List<Member> memberList = memberRepository.findAll();
        List<ResponseMemberDto> responseMemberDtoList = memberList.stream()
                .map(ResponseMemberDto::from)
                .collect(Collectors.toList());
        return responseMemberDtoList;
    }

    public ResponseMemberDto getMyInfoBySecurity() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(ResponseMemberDto::from)
                .orElseThrow(() -> new RuntimeException(MemberErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }

    @Transactional
    public ResponseMemberDto modifyMemberInfo(RequestChangeMemberInfoDto requestChangeMemberInfoDto) {
        Member member =
                memberRepository.findById(requestChangeMemberInfoDto.getId()).orElseThrow(() -> new RuntimeException(MemberErrorCode.MEMBER_NOT_FOUND.getMessage()));
        member.updateMemberInfo(requestChangeMemberInfoDto);
        return ResponseMemberDto.from(memberRepository.save(member));
    }

    @Transactional
    public ResponseMemberDto changeMemberName(String newName) {
        Member member =
                memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException(
                MemberErrorCode.MEMBER_NOT_FOUND.getMessage()));
        member.updateName(newName);
        return ResponseMemberDto.from(memberRepository.save(member));
    }

    @Transactional
    public ResponseMemberDto changeMemberPassword(RequestChangePasswordDto requestChangePasswordDto) {
        Member member = memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RuntimeException(MemberErrorCode.MEMBER_NOT_FOUND.getMessage()));
        if (!passwordEncoder.matches(requestChangePasswordDto.getExPassword(), member.getPassword())) {
            throw new RuntimeException(MemberErrorCode.MEMBER_PASSWORD_NOT_MATCH.getMessage());
        }
        member.updatePassword(passwordEncoder.encode((requestChangePasswordDto.getNewPassword())));
        return ResponseMemberDto.from(memberRepository.save(member));
    }

    @Transactional
    public ResponseMemberDto memberWithdrawal(Long memberId) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException(MemberErrorCode.MEMBER_NOT_FOUND.getMessage()));
        member.updateActivated(false);
        return ResponseMemberDto.from(memberRepository.save(member));
    }
}
