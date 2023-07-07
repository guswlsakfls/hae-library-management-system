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
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .role(Role.valueOf("ROLE_USER"))
                .build();

        return ResponseMemberDto.from(memberRepository.save(member));
    }

    /**
     * 모든 회원 목록을 검색어 그리고 페이징하여 조회합니다.
     *
     * @param search 검색어
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @param role   역할
     * @param sort   정렬
     * @return 회원 정보 페이지
     */
    public Page<ResponseMemberDto> getAllMember(String search, int page, int size, String role,
                                                String sort) {
        // 페이지 정보를 설정합니다. 페이지는 0부터 시작하며, 정렬은 'createdAt' 컬럼을 기준으로 최신순, 오래된순으로 정렬합니다.
        Sort.Direction direction = sort.equals("최신순") ?  Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        Specification<Member> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 검색어가 null이거나 비어있는 경우를 제외하고, 검색어를 포함하는 회원 정보를 반환합니다.
            if (search != null && !search.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%"));
            }

            // 역할에 따라 검색을 합니다.
            if (!"전체".equals(role)) {
                Role roleEnum = "관리자".equals(role) ? Role.ROLE_ADMIN : Role.ROLE_USER;
                predicates.add(cb.equal(root.get("role"), roleEnum));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };


        // 검색어에 따른 동적 쿼리로 회원 정보를 페이지 단위로 가져옵니다.
        Page<Member> memberList = memberRepository.findAll(spec, pageable);
        // 가져온 회원 정보를 DTO로 변환합니다.
        Page<ResponseMemberDto> responseMemberDtoList = memberList.map(member -> {
            // 각 Member 객체를 ResponseMemberDto로 변환합니다.
            ResponseMemberDto dto = ResponseMemberDto.from(member);
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
        // jwt토큰에서 받아온 사용자의 이메일을 기준으로 회원 정보를 조회합니다.
        return memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
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
        if (memberRepository.existsByEmailAndIdIsNot(requestChangeMemberInfoDto.getEmail(), requestChangeMemberInfoDto.getId())) {
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
                memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

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
     * @return 변경된 회원 정보 DTO
     */
    @Transactional
    public ResponseMemberDto memberWithdrawal() {
        Member member =
                memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateActivated(false);
        return ResponseMemberDto.from(memberRepository.save(member));
    }
}
