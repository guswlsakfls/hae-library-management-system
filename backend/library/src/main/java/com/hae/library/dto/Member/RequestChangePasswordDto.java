package com.hae.library.dto.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestChangePasswordDto {
    private String email;
    private String exPassword;
    private String newPassword;
}
