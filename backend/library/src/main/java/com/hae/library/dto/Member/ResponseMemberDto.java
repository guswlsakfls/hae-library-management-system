package com.hae.library.dto.Member;

import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseMemberDto {
    private Long id;
    private String email;
    private String name;
    private Role role;
    private LocalDateTime penaltyEndDate;
    private boolean activated;


    public static ResponseMemberDto from(Member member) {
        return ResponseMemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .penaltyEndDate(member.getPenaltyEndDate())
                .activated(member.isActivated())
                .build();
    }
}
