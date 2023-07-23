package com.hae.library.dto.Member.Request;

import com.hae.library.domain.Enum.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 회원 정보 변경에 대한 요청 DTO입니다
@Getter
@NoArgsConstructor
public class RequestChangeMemberInfoDto {
    @NotNull(message = "id는 필수 입력 값입니다.")
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Future(message = "연체 종료일은 현재 날짜보다 미래여야 합니다.")
    private LocalDateTime penaltyEndDate;

    @NotNull(message = "role은 필수 입력 값입니다.")
    private Role role;

    @NotNull(message = "activated는 필수 입력 값입니다.")
    private Boolean activated; // null을 허용하지 않기 위해 Boolean으로 선언

    @Builder
    public RequestChangeMemberInfoDto(Long id, String email, LocalDateTime penaltyEndDate, Role role, Boolean activated) {
        this.id = id;
        this.email = email;
        this.penaltyEndDate = penaltyEndDate;
        this.role = role;
        this.activated = activated;
    }
}
