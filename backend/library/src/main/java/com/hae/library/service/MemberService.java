package com.hae.library.service;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Enum.Role;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    /**
     * 회원가입을 처리합니다.
     *
     * @param requestSignupDto 회원 가입 정보 DTO
     * @return 회원 정보 DTO
     */
    public ResponseMemberDto signup(RequestSignupDto requestSignupDto) {
        // 입력받은 패스워드와 패스워드 재확인이 일치하지 않으면 예외를 발생시킵니다.
        if (!requestSignupDto.isPasswordMatching()) {
            throw new RestApiException(MemberErrorCode.MEMBER_PASSWORD_NOT_MATCH);
        }

        // 이메일이 이미 존재하면 예외를 발생시킵니다.
        if (memberRepository.findByEmail(requestSignupDto.getEmail()).orElse(null) != null) {
            throw new RestApiException(MemberErrorCode.MEMBER_ALREADY_EXIST);
        }

        // Member 객체를 빌드하고 저장합니다. 비밀번호는 암호화하여 저장합니다.
        Member member = Member.builder()
                .email(requestSignupDto.getEmail())
                .password(passwordEncoder.encode(requestSignupDto.getPassword()))
                .role(Role.valueOf("USER"))
                .build();

        return ResponseMemberDto.from(memberRepository.save(member));
    }

    /**
     * 모든 회원 목록을 검색어 그리고 페이징하여 조회합니다.
     *
     * @param search 검색어
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @return 회원 정보 페이지
     */
    public Page<ResponseMemberDto> getAllMember(String search, int page, int size) {
        // 페이지 정보를 설정합니다. 페이지는 0부터 시작하며, 정렬은 'createdAt' 컬럼을 기준으로 오름차순으로 합니다.
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        // Specification을 이용해 동적 쿼리를 생성합니다.
        Specification<Member> spec = (root, query, cb) -> {
            // 검색어가 null이거나 비어있는 경우, 모든 회원 정보를 반환합니다.
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }
            // 검색어가 존재하는 경우, 이메일이 검색어를 포함하는 회원 정보를 반환합니다.
            return cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%");
        };

        // 검색어에 따른 동적 쿼리로 회원 정보를 페이지 단위로 가져옵니다.
        Page<Member> memberList = memberRepository.findAll(spec, pageable);
        // 가져온 회원 정보를 DTO로 변환합니다.
        Page<ResponseMemberDto> responseMemberDtoList = memberList.map(member -> {
            // 각 Member 객체를 ResponseMemberDto로 변환합니다.
            ResponseMemberDto dto = ResponseMemberDto.from(member);
            // 각 Member의 대출 목록을 가져와 ResponseLendingDto 리스트로 변환하고, 이를 ResponseMemberDto에 설정합니다.
            dto.updateLendingList(member.getLendingList().stream()
                    .map(lending -> ResponseLendingDto.from(lending))
                    .collect(Collectors.toList()));
            return dto;
        });

        // DTO로 변환된 회원 정보 페이지를 반환합니다.
        return responseMemberDtoList;
    }


    /**
     * 현재 로그인한 사용자의 정보를 조회합니다.
     *
     * @return 현재 로그인한 사용자의 정보 DTO
     */
    public ResponseMemberDto getMyInfoBySecurity() {
        return memberRepository.findByEmail(SecurityUtil.getCurrentMemberId())
                .map(ResponseMemberDto::from)
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 이메일을 기준으로 회원 정보를 조회합니다.
     *
     * @param requestEmailDto 이메일 정보 DTO
     * @return 회원 정보 DTO
     */
    public ResponseMemberDto getMemberByEmail(RequestEmailDto requestEmailDto) {
        // 이메일로 사용자를 찾습니다.
        Optional<Member> member = memberRepository.findByEmail(requestEmailDto.getEmail());

        // 만약 사용자를 찾지 못했다면, 예외를 발생시킵니다.
        if (member.isEmpty()) {
            throw new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        ResponseMemberDto responseMemberDto = ResponseMemberDto.from(member.get());

        // 해당 사용자가 대출한 책 목록을 가져와 DTO로 변환합니다. 그리고 이를 사용자 DTO에 설정합니다.
        responseMemberDto.updateLendingList(member.get().getLendingList().stream()
                .map(lending -> ResponseLendingDto.from(lending))
                .collect(Collectors.toList()));


        return responseMemberDto;
    }


    /**
     * 특정 회원의 정보를 수정합니다.
     *
     * @param requestChangeMemberInfoDto 변경할 회원 정보 DTO
     * @return 변경된 회원 정보 DTO
     */
    @Transactional
    public ResponseMemberDto modifyMemberInfo(RequestChangeMemberInfoDto requestChangeMemberInfoDto) {
        // 변경하려는 이메일이 이미 존재하는 경우, 예외를 발생시킵니다.
        if (memberRepository.existsByEmail(requestChangeMemberInfoDto.getEmail())) {
            throw new RestApiException(MemberErrorCode.MEMBER_DUPLICATE_EMAIL);
        }

        // 요청받은 ID로 회원 정보를 찾습니다. 없다면, 예외를 발생시킵니다.
        Member member =
                memberRepository.findById(requestChangeMemberInfoDto.getId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateMemberInfo(requestChangeMemberInfoDto);
        return ResponseMemberDto.from(memberRepository.save(member));
    }

    /**
     * 회원의 비밀번호를 변경합니다.
     *
     * @param requestChangePasswordDto 비밀번호 변경 정보 DTO
     * @return 변경된 회원 정보 DTO
     */
    @Transactional
    public ResponseMemberDto changeMemberPassword(RequestChangePasswordDto requestChangePasswordDto) {
        // 현재 보안 컨텍스트에서 인증된 사용자의 이메일로 회원 정보를 찾습니다. 없다면, 예외를 발생시킵니다.
        Member member =
                memberRepository.findByEmail(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 요청으로 받은 이전 비밀번호와 회원의 현재 비밀번호가 일치하지 않는 경우, 예외를 발생시킵니다.
        if (!passwordEncoder.matches(requestChangePasswordDto.getExPassword(), member.getPassword())) {
            throw new RestApiException(MemberErrorCode.MEMBER_PASSWORD_NOT_MATCH);
        }

        // 회원의 비밀번호를 새 비밀번호로 업데이트하고 이를 암호화합니다.
        member.updatePassword(passwordEncoder.encode((requestChangePasswordDto.getNewPassword())));

        return ResponseMemberDto.from(memberRepository.save(member));
    }

    /**
     * 회원 탈퇴 처리를 합니다. 회원의 activated 상태를 false로 변경합니다.
     *
     * @param memberId 회원 ID
     * @return 변경된 회원 정보 DTO
     */
    @Transactional
    public ResponseMemberDto memberWithdrawal(Long memberId) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateActivated(false);
        return ResponseMemberDto.from(memberRepository.save(member));
    }
}
