package com.hae.library.dto.Member;

import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
//import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestLoginDto {
    @NotBlank
    @Size(min = 3, max = 50)
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    private String password;
}
