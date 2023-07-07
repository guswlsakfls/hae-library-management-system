package com.hae.library.controller;

import com.hae.library.config.Security.RoleInterface;
import com.hae.library.dto.Member.*;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.service.AuthService;
import com.hae.library.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class MemberContorller {
    private final MemberService memberService;

    // 회원가입 요청을 합니다
    @PostMapping(value = "/signup")
    public ResponseResultDto<Object> signUp(@RequestBody @Valid RequestSignupDto requestSignupDto) {
        log.info("회원가입 요청: [POST] /member/signup - {}", requestSignupDto.toString());
        ResponseMemberDto responseMemberDto = memberService.signup(requestSignupDto);

        log.info("회원가입에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원가입에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

    // 회원 정보 전체를 조회합니다.
//    @RoleInterface.AdminAuthorize
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

    // 내 프로필 정보를 조회합니다.
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

    // 회원 정보를 조회합니다.
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

    // 회원 정보를 수정합니다.
//    @RoleInterface.AdminAuthorize
    @PutMapping(value = "/admin/member/update")
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

    // 회원 비밀번호를 변경합니다.
    @PutMapping(value = "/member/changePassword")
    public ResponseResultDto<Object> updateMemberPassword(@RequestBody RequestChangePasswordDto requestChangePasswordDto) {
        log.info("회원 비밀번호 변경: [PUT] /member/changePassword - {}", requestChangePasswordDto.toString());
        memberService.changeMemberPassword(requestChangePasswordDto);

        log.info("회원 비밀번호 변경에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 비밀번호 변경에 성공하였습니다")
                .build();
    }


    // TODO: 회원 탈퇴시 boolean으로 처리(1: 회원, 0: 탈퇴)
    @PutMapping(value = "/member/withdrawal/me")
    public ResponseResultDto<Object> memberWithdrawal() {
        log.info("회원 탈퇴: [PUT] /member/withdrawal");
        memberService.memberWithdrawal();

        log.info("회원 탈퇴에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 탈퇴에 성공하였습니다")
                .build();
    }

}
