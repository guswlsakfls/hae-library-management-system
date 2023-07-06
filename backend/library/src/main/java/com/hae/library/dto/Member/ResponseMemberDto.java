package com.hae.library.dto.Member;

import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import com.hae.library.dto.Lending.ResponseLendingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseMemberDto {
    private Long id;
    private String email;
    private Role role;
    private LocalDateTime penaltyEndDate;
    private int lendingCount;
    private String createdAt;
    private String updatedAt;
    private boolean activated;


    public static ResponseMemberDto from(Member member) {
        return ResponseMemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole())
                .penaltyEndDate(member.getPenaltyEndDate())
                .lendingCount(member.getLendingCount())
                .activated(member.isActivated())
                .createdAt(member.getCreatedAt().toLocalDate().toString())
                .updatedAt(member.getUpdatedAt().toLocalDate().toString())
                .build();
    }

    // 회원의 대출 횟수를 업데이트 합니다.
    public void updateLendingCount(int lendingCount) {
        this.lendingCount = lendingCount;
    }
}
