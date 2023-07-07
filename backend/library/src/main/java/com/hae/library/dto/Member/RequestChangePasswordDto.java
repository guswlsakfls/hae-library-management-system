package com.hae.library.dto.Member;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestChangePasswordDto {
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요")
    private String nowPassword;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요")
    private String newPassword;
}
