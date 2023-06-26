package com.hae.library.controller;

import com.hae.library.config.Security.RoleInterface;
import com.hae.library.dto.Member.RequestMemberDto;
import com.hae.library.dto.Member.RequestSignupDto;
import com.hae.library.dto.Member.ResponseMemberDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.service.AuthService;
import com.hae.library.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class MemberContorller {
    MemberService memberService;
    AuthService authService;

    @PostMapping(value = "/member/signup")
    public ResponseResultDto<Object> signUp(@RequestBody RequestMemberDto requestMemberDto) {
        ResponseMemberDto responseMemberDto = authService.signup(requestMemberDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원가입에 성공하였습니다")
                .data(responseMemberDto)
                .build();
    }

    @RoleInterface.AdminAuthorize
    @GetMapping(value = "/member/all")
    public String getAllMember() {
        return "MemberContorller";
    }

    @GetMapping(value = "/member/{memberId}/me")
    public String getMemberMe() {
        return "MemberContorller";
    }

    @PutMapping(value = "/member/{memberId}/password")
    public String updateMemberPassword() {
        return "MemberContorller";
    }

    @PutMapping(value = "/member/{memberId}/name")
    public String updateMemberName() {
        return "MemberContorller";
    }

    // TODO: 회원 탈퇴시 boolean으로 처리(1: 회원, 0: 탈퇴)
    @PutMapping(value = "/member/{memberId}/Update")
    public String updateMember() {
        return "MemberContorller";
    }

}
