package com.hae.library.dto.Member;

import com.hae.library.domain.Enum.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RequestChangeMemberInfoDto {
    private Long id;
    private String email;
    private LocalDateTime penaltyEndDate;
    private Role role;
    private boolean activated;
}
