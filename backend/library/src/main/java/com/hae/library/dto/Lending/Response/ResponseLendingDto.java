package com.hae.library.dto.Lending.Response;

import com.hae.library.domain.Lending;
import lombok.Getter;

// 대출 조회에 대한 응답 DTO입니다
@Getter
public class ResponseLendingDto {
    private Long id;
    private String bookTitle;
    private String bookCallSign;
    private String userEmail;
    private String lendingLibrarianEmail;
    private String lendingCondition;
    private String returningLibrarianEmail;
    private String returningCondition;
    private String createdAt;
    private String returningAt;

    public static ResponseLendingDto from(Lending lending) {
        ResponseLendingDto dto = new ResponseLendingDto();
        dto.id = lending.getId();
        dto.bookTitle = lending.getBook().getBookInfo().getTitle();
        dto.bookCallSign = lending.getBook().getCallSign();
        dto.userEmail = lending.getLendingUser().getEmail();
        dto.lendingLibrarianEmail = lending.getLendingLibrarian().getEmail();
        dto.lendingCondition = lending.getLendingCondition();
        dto.createdAt = lending.getCreatedAt().toLocalDate().toString();
        if (lending.getReturningLibrarian() != null) {
            dto.returningLibrarianEmail = lending.getReturningLibrarian().getEmail();
            dto.returningCondition = lending.getReturningCondition();
            dto.returningAt = lending.getReturningEndAt().toLocalDate().toString();
        }
        return dto;
    }
}
