package com.hae.library.controller;

import com.hae.library.dto.Member.Request.RequestChangeMemberInfoDto;
import com.hae.library.dto.Member.Request.RequestChangePasswordDto;
import com.hae.library.dto.Member.Request.RequestEmailDto;
import com.hae.library.dto.Member.Request.RequestSignupDto;
import com.hae.library.dto.Member.Response.ResponseMemberDto;
import com.hae.library.dto.Common.ResponseResultDto;
import com.hae.library.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class MemberContorller {
    private final MemberService memberService;

    /**
     * 회원가입 요청을 받아 처리합니다.
     *
     * @param requestSignupDto
     * @return "회원가입에 성공하였습니다" 메시지
     */
    @PostMapping(value = "/signup")
    public ResponseResultDto<Object> signUp(@RequestBody @Valid RequestSignupDto requestSignupDto) {
        log.info("회원가입 요청: [POST] /member/signup - {}", requestSignupDto.toString());
        memberService.signup(requestSignupDto);

        log.info("회원가입에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원가입에 성공하였습니다")
                .build();
    }

    /**
     * 검색 조건을 통해 회원 목록을 조회합니다.
     *
     * @param search
     * @param page
     * @param size
     * @param role
     * @param sort
     * @return 회원 정보 목록과 페이지 네이션 정보
     */
    @GetMapping(value = "/admin/memberinfo/all")
    public ResponseResultDto<Object> getAllMember(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String sort
    ) {
        log.info("모든 회원 정보 조회: [GET] /member/all - search: {}, page: {}, size: {}, role: {}, " +
                        "sort: {}",
                search, page,
                size, role, sort);
        // 검색 키워드와 페이진이션 정보를 인자로 주어 회원 정보를 가져옵니다.
        Page<ResponseMemberDto> responseMemberDtoList = memberService.getAllMember(search, page,
                size, role, sort);
        // 회원 정보 리스트 와 페이지 네이션 정보를 데이터로 설정합니다.
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("memberList", responseMemberDtoList.getContent());
        responseData.put("totalElements", responseMemberDtoList.getTotalElements());
        responseData.put("currentPage", responseMemberDtoList.getNumber());
        responseData.put("size", responseMemberDtoList.getSize());

        log.info("회원 전체 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 전체 조회에 성공하였습니다")
                .data(responseData)
                .build();
    }

    /**
     * 내 정보를 조회합니다.
     *
     * @return 내 정보
     */
    @GetMapping(value = "/member/memberinfo/me")
    public ResponseResultDto<Object> getMemberMe() {
        log.info("내 정보 조회: [GET] /memberInfo/me");
        ResponseMemberDto responseMemberDto = memberService.getMyInfoBySecurity();

        log.info("내 정보 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 정보 조회에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

    /**
     * 이메일을 통해 회원 정보를 조회합니다.
     *
     * @param requestEmailDto
     * @return 회원 정보
     */
    @PostMapping(value = "/admin/memberinfo")
    public ResponseResultDto<Object> getMemberByEmail(@RequestBody @Valid RequestEmailDto requestEmailDto) {
        log.info("회원 정보 조회: [POST] /memberInfo - {}", requestEmailDto.toString());
        ResponseMemberDto responseMemberDto = memberService.getMemberByEmail(requestEmailDto);

        log.info("회원 정보 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 정보 조회에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

    /**
     * 회원 정보를 수정합니다.
     *
     * @param requestChangeMemberInfoDto
     * @return 수정된 회원 정보
     */
    @PutMapping(value = "/admin/member")
    public ResponseResultDto<Object> modifyMemberInfo(@RequestBody @Valid RequestChangeMemberInfoDto requestChangeMemberInfoDto) {
        log.info("회원 정보 수정: [PUT] /member/update - {}", requestChangeMemberInfoDto.toString());
        ResponseMemberDto responseMemberDto =
                memberService.modifyMemberInfo(requestChangeMemberInfoDto);

        log.info("회원 정보 수정에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 정보 수정에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

    /**
     * 회원 비밀번호를 변경합니다.
     *
     * @param requestChangePasswordDto
     * @return "회원 비밀번호 변경에 성공하였습니다" 메시지
     */
    @PutMapping(value = "/member/changePassword")
    public ResponseResultDto<Object> updateMemberPassword(@RequestBody @Valid RequestChangePasswordDto requestChangePasswordDto) {
        log.info("회원 비밀번호 변경: [PUT] /member/changePassword - {}", requestChangePasswordDto.toString());
        memberService.changeMemberPassword(requestChangePasswordDto);

        log.info("회원 비밀번호 변경에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 비밀번호 변경에 성공하였습니다")
                .build();
    }


    /**
     * 회원을 휴면계정으로 전환합니다.
     *
     * @return "회원이 휴면계정 전환에 성공하였습니다" 메시지
     */
    @PutMapping(value = "/member/withdrawal/me")
    public ResponseResultDto<Object> memberWithdrawal() {
        log.info("회원 휴면계정 전환: [PUT] /member/withdrawal");
        memberService.memberWithdrawal();

        log.info("회원이 휴면계정 전환에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원이 휴면계정 전환에 성공하였습니다")
                .build();
    }

    /**
     * 회원을 삭제합니다.
     *
     * @param memberId
     * @return "회원 삭제에 성공하였습니다" 메시지
     */
    @DeleteMapping(value = "/admin/member/{memberId}")
    public ResponseResultDto<Object> deleteMember(@PathVariable Long memberId) {
        log.info("회원 삭제: [DELETE] /member - {}", memberId);
        memberService.deleteMember(memberId);

        log.info("회원 삭제에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 삭제에 성공하였습니다")
                .build();
    }
}
