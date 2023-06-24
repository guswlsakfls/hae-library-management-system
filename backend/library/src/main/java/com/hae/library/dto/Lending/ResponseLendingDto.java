package com.hae.library.dto.Lending;

import com.hae.library.domain.Lending;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseLendingDto {
    private Long id;
    private Long bookId;
    private Long lendingLibrarianId;
    private String lendingCondition;
    private Long returningLibrarianId;
    private String returningCondition;
    private String returningAt;
    private boolean renew;

    // 생성자, 게터/세터 생략

    public static ResponseLendingDto from(Lending lending) {
        ResponseLendingDto dto = new ResponseLendingDto();
        dto.setId(lending.getId());
//        dto.setBookId(lending.getBook().getId());
//        dto.setLendingLibrarianId(lending.getLendingLibrarian().getId());
        dto.setLendingCondition(lending.getLendingCondition());
//        dto.setReturningLibrarianId(lending.getReturningLibrarian().getId());
        dto.setReturningCondition(lending.getReturningCondition());
        dto.setReturningAt(lending.getReturningAt().toString());
        dto.setRenew(lending.isRenew());
        return dto;
    }
}
