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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class MemberContorller {
    MemberService memberService;
    AuthService authService;

    @PostMapping(value = "/member/signup")
    public ResponseResultDto<Object> signUp(@RequestBody @Valid RequestSignupDto requestSignupDto) {
        log.info("회원가입 요청 : {}", requestSignupDto);
        ResponseMemberDto responseMemberDto = memberService.signup(requestSignupDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원가입에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

//    @RoleInterface.AdminAuthorize
    @GetMapping(value = "/member/all")
    public ResponseResultDto<Object> getAllMember() {
        List<ResponseMemberDto> responseMemberDtoList = memberService.getAllMember();
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 전체 조회에 성공하였습니다")
                .data(responseMemberDtoList)
                .build();
    }

    @GetMapping(value = "/memberInfo/me")
    public ResponseResultDto<Object> getMemberMe() {
        ResponseMemberDto responseMemberDto = memberService.getMyInfoBySecurity();
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 정보 조회에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

    @PostMapping(value = "/memberInfo")
    public ResponseResultDto<Object> getMemberByEmail(@RequestBody @Valid RequestEmailDto requestEmailDto) {
        ResponseMemberDto responseMemberDto = memberService.getMemberByEmail(requestEmailDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 정보 조회에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

//    @RoleInterface.AdminAuthorize
    @PutMapping(value = "/member/modify")
    public ResponseResultDto<Object> modifyMemberInfo(@RequestBody RequestChangeMemberInfoDto requestChangeMemberInfoDto) {
        ResponseMemberDto responseMemberDto =
                memberService.modifyMemberInfo(requestChangeMemberInfoDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 정보 수정에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

    @PutMapping(value = "/member/changePassword")
    public ResponseResultDto<Object> updateMemberPassword(@RequestBody RequestChangePasswordDto requestChangePasswordDto) {
        ResponseMemberDto responseMemberDto =
                memberService.changeMemberPassword(requestChangePasswordDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 비밀번호 변경에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }


    // TODO: 회원 탈퇴시 boolean으로 처리(1: 회원, 0: 탈퇴)
    @PutMapping(value = "/member/withdrawal")
    public ResponseResultDto<Object> memberWithdrawal(@RequestBody Long memberId) {
        memberService.memberWithdrawal(memberId);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 탈퇴에 성공하였습니다")
                .build();
    }

}
