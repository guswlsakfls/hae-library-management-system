package com.hae.library.dto.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestSignupDto {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$", message = "비밀번호는 영문, 숫자를 포함한 8~20자리여야 합니다")
    private String password;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$", message = "비밀번호 확인은 영문, 숫자를 포함한 " +
            "8~20자리여야" +
            " 합니다")
    private String checkPassword;
    @Builder
    public RequestSignupDto(String email, String password, String checkPassword) {
        this.email = email;
        this.password = password;
        this.checkPassword = checkPassword;
    }

    public boolean isPasswordMatching() {
        return this.password.equals(this.checkPassword);
    }
}
