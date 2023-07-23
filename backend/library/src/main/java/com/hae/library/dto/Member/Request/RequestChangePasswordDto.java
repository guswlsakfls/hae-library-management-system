package com.hae.library.dto.Member.Request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 비밀번호 변경에 대한 요청 DTO입니다
@Getter
@NoArgsConstructor
public class RequestChangePasswordDto {
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요")
    private String nowPassword;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요")
    private String newPassword;

    @Builder
    public RequestChangePasswordDto(String nowPassword, String newPassword) {
        this.nowPassword = nowPassword;
        this.newPassword = newPassword;
    }
}
