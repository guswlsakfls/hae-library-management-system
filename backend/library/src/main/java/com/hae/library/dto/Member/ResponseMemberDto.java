package com.hae.library.dto.Member;

import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseMemberDto {
    private String email;
    private String nickname;


    public static ResponseMemberDto from(Member member) {
        return ResponseMemberDto.builder()
                .email(member.getEmail())
                .nickname(member.getName())
                .build();
    }
}
