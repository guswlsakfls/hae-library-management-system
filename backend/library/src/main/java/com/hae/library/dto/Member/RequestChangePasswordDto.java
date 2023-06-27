package com.hae.library.dto.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestChangePasswordDto {
    private String id;
    private String exPassword;
    private String newPassword;
}
