package com.hae.library.dto.Lending;

import com.hae.library.domain.Lending;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseMemberLendingDto {
    private Long lendingid;
    private String bookTitle;
    private String bookCallSign;
    private String userEmail;
    private String returningEndAt;
    private String createdAt;
    private boolean renew;

    public static ResponseMemberLendingDto from(Lending lending) {
        ResponseMemberLendingDto dto = new ResponseMemberLendingDto();
        dto.lendingid = lending.getId();
        dto.bookTitle = lending.getBook().getBookInfo().getTitle();
        dto.bookCallSign = lending.getBook().getCallSign();
        dto.userEmail = lending.getUser().getEmail();
        // 반납일이 없으면 null이므로 null이 아닐때만 반환
        if (lending.getReturningEndAt() != null) {
            dto.returningEndAt = lending.getReturningEndAt().toLocalDate().toString();
        }
        dto.createdAt = lending.getCreatedAt().toLocalDate().toString();
        dto.renew = lending.isRenew();
        return dto;
    }
}
