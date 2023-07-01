package com.hae.library.dto.Lending;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class RequestLendingDto {
    @NotNull(message = "책 아이디는 필수 입력 값입니다.")
    private Long bookId;

    @NotNull(message = "사용자 아이디는 필수 입력 값입니다.")
    private Long userId;

    @Null(message = "대출 사서는 입력할 필요가 없습니다.")
    private Long lendingLibrarianId;

    @Size(min = 4, max = 100, message = "대출 특이사항은 4~100자 이내로 입력해주세요.")
    private String lendingCondition;

    @Builder
    public RequestLendingDto(Long bookId, Long userId, Long lendingLibrarianId, String lendingCondition, LocalDateTime returningAt) {
        this.bookId = bookId;
        this.userId = userId;
        this.lendingLibrarianId = lendingLibrarianId;
        this.lendingCondition = lendingCondition;
    }
}
