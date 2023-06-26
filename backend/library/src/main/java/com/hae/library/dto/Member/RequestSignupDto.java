package com.hae.library.dto.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestSignupDto {
    private String name;
    private String email;
    private String password;

    @Builder
    public RequestSignupDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
