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
    public ResponseResultDto<Object> getAllMember(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ) {
        // 검색 키워드와 페이진이션 정보를 인자로 주어 회원 정보를 가져옵니다.
        Page<ResponseMemberDto> responseMemberDtoList = memberService.getAllMember(search, page,
                size);
        // 회원 정보 리스트 와 페이지 네이션 정보를 데이터로 설정합니다.
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("memberList", responseMemberDtoList.getContent());
        responseData.put("totalElements", responseMemberDtoList.getTotalElements());
        responseData.put("currentPage", responseMemberDtoList.getNumber());
        responseData.put("size", responseMemberDtoList.getSize());
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("회원 전체 조회에 성공하였습니다")
                .data(responseData)
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
    @PutMapping(value = "/member/update")
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
