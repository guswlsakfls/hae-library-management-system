package com.hae.library.dto.Lending.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 반납에 대한 요청 DTO입니다
@Getter
@NoArgsConstructor
public class RequestReturningDto {
    // TODO: 책 정보를 가지고 반납할지, 반납 리스트에서 검색해서 반납할지 결정 해야함.
    @NotNull(message = "대출 ID는 필수 입력 값입니다.")
    private Long lendingId;

//    @NotNull(message = "책 ID는 필수 입력 값입니다.")
//    private Long bookId;

    // TODO: 액세스토큰으로 받아올 수 있으면 필요없다.
//    @NotNull(message = "반납한 사서 ID는 필수 입력 값입니다.")
//    private Long returningLibrarianId;

    @NotBlank(message = "반납 상태는 필수 입력 값입니다.")
    @Size(min = 4, max = 100, message = "반납 상태는 4~100자 이내로 입력해주세요.")
    private String returningCondition;

    @Builder
    public RequestReturningDto(Long lendingId, Long returningLibrarianId, String returningCondition) {
        this.lendingId = lendingId;
//        this.returningLibrarianId = returningLibrarianId;
        this.returningCondition = returningCondition;
    }
}
