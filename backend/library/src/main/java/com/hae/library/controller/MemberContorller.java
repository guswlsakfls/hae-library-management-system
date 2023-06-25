package com.hae.library.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class MemberContorller {

    @PostMapping(value = "/member/signup")
    public String signUp() {
        return "MemberContorller";
    }

    @PostMapping(value = "/member/login")
    public String loginMember() {
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
    public String updateMembername() {
        return "MemberContorller";
    }

    @DeleteMapping(value = "/member/{memberId}/delete")
    public String deleteMember() {
        return "MemberContorller";
    }

}
