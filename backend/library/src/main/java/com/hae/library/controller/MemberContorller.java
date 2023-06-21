package com.hae.library.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class MemberContorller {

    @PostMapping(value = "/v1/member/signup")
    public String signUp() {
        return "MemberContorller";
    }

    @PostMapping(value = "/v1/member/login")
    public String loginMember() {
        return "MemberContorller";
    }

    @GetMapping(value = "/v1/member/{memberId}/me")
    public String getMemberMe() {
        return "MemberContorller";
    }

    @PutMapping(value = "/v1/member/{memberId}/password")
    public String updateMemberPassword() {
        return "MemberContorller";
    }

    @PutMapping(value = "/v1/member/{memberId}/name")
    public String updateMembername() {
        return "MemberContorller";
    }

    @DeleteMapping(value = "/v1/member/{memberId}/delete")
    public String deleteMember() {
        return "MemberContorller";
    }

}
