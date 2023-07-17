package com.hae.library.dto.Lending.Response;

import com.hae.library.domain.Lending;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 반납을 위한 대출 정보를 반환하는 DTO
@Getter
@NoArgsConstructor
public class ResponseLendingInfoForReturningDto {
    private Long lendingId;
    private String userEmail;
    private int lendingCount;
    private LocalDateTime userPenaltyEndDate;
    private String bookTitle;
    private String bookCallSign;
    private LocalDateTime createdAt;
    private String returningEndAt;

    public static ResponseLendingInfoForReturningDto from(Lending lending) {
        ResponseLendingInfoForReturningDto dto = new ResponseLendingInfoForReturningDto();
        dto.lendingId = lending.getId();
        dto.userEmail = lending.getLendingUser().getEmail();
        dto.lendingCount = lending.getLendingUser().getLendingCount();
        dto.userPenaltyEndDate = lending.getLendingUser().getPenaltyEndDate();
        dto.bookTitle = lending.getBook().getBookInfo().getTitle();
        dto.bookCallSign = lending.getBook().getCallSign();
        dto.createdAt = lending.getCreatedAt();
        if (lending.getReturningEndAt() != null) {
            dto.returningEndAt = lending.getReturningEndAt().toLocalDate().toString();
        }
        return dto;
    }
}
