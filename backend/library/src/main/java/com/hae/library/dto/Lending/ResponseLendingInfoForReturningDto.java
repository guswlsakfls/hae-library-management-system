package com.hae.library.dto.Lending;

import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private String returningEndAt;

    public static ResponseLendingInfoForReturningDto from(Lending lending) {
        ResponseLendingInfoForReturningDto dto = new ResponseLendingInfoForReturningDto();
        dto.lendingId = lending.getId();
        dto.userEmail = lending.getUser().getEmail();
        dto.lendingCount = lending.getUser().getLendingList().size();
        dto.userPenaltyEndDate = lending.getUser().getPenaltyEndDate();
        dto.bookTitle = lending.getBook().getBookInfo().getTitle();
        dto.bookCallSign = lending.getBook().getCallSign();
        dto.returningEndAt = lending.getReturningEndAt().toLocalDate().toString();
        return dto;
    }
}
