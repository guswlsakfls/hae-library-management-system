package com.hae.library.service;

import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.Lending.ResponseLendingDto;
import com.hae.library.dto.Member.*;
import com.hae.library.global.Exception.RestApiException;
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
import java.util.Optional;
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
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public ResponseMemberDto getMemberByEmail(RequestEmailDto requestEmailDto) {
        // 이메일로 사용자를 찾는다.
        Optional<Member> member = memberRepository.findByEmail(requestEmailDto.getEmail());

        // 만약 사용자를 찾지 못했다면, MEMBER_NOT_FOUND 오류를 발생시킨다.
        if (member.isEmpty()) {
            throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        // 사용자를 찾았다면, 사용자 정보를 DTO 객체로 변환한다.
        ResponseMemberDto responseMemberDto = ResponseMemberDto.from(member.get());

        // 해당 사용자가 대출한 책 목록을 가져와 DTO로 변환한다. 그리고 이를 사용자 DTO에 설정한다.
        responseMemberDto.updateLendingList(member.get().getLendingList().stream()
                .map(lending -> ResponseLendingDto.from(lending))
                .collect(Collectors.toList()));

        // 최종적으로 사용자 정보를 담은 DTO를 반환한다.
        return responseMemberDto;
    }


    @Transactional
    public ResponseMemberDto modifyMemberInfo(RequestChangeMemberInfoDto requestChangeMemberInfoDto) {
        Member member =
                memberRepository.findById(requestChangeMemberInfoDto.getId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateMemberInfo(requestChangeMemberInfoDto);
        return ResponseMemberDto.from(memberRepository.save(member));
    }

    @Transactional
    public ResponseMemberDto changeMemberName(String newName) {
        Member member =
                memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RestApiException(
                MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateName(newName);
        return ResponseMemberDto.from(memberRepository.save(member));
    }

    @Transactional
    public ResponseMemberDto changeMemberPassword(RequestChangePasswordDto requestChangePasswordDto) {
        Member member =
                memberRepository.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!passwordEncoder.matches(requestChangePasswordDto.getExPassword(), member.getPassword())) {
            throw new RestApiException(MemberErrorCode.MEMBER_PASSWORD_NOT_MATCH);
        }
        member.updatePassword(passwordEncoder.encode((requestChangePasswordDto.getNewPassword())));
        return ResponseMemberDto.from(memberRepository.save(member));
    }

    @Transactional
    public ResponseMemberDto memberWithdrawal(Long memberId) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateActivated(false);
        return ResponseMemberDto.from(memberRepository.save(member));
    }
}
