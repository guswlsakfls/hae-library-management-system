package com.hae.library.dto.Lending;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestReturningDto {
    @NotNull(message = "대출 ID는 필수 입력 값입니다.")
    private Long lendingId;

    @NotNull(message = "반납한 사서 ID는 필수 입력 값입니다.")
    private Long returningLibrarianId;

    @NotBlank(message = "반납 상태는 필수 입력 값입니다.")
    @Size(min = 4, max = 100, message = "반납 상태는 4~100자 이내로 입력해주세요.")
    private String returningCondition;

    @Builder
    public RequestReturningDto(Long lendingId, Long returningLibrarianId, String returningCondition) {
        this.lendingId = lendingId;
        this.returningLibrarianId = returningLibrarianId;
        this.returningCondition = returningCondition;
    }
}
