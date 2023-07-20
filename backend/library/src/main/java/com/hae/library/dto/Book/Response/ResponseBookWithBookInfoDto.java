package com.hae.library.dto.Book.Response;

import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.BookInfo.Response.ResponseBookInfoDto;
import lombok.Builder;
import lombok.Getter;

// ResponseBookWithBookInfoDto는 Book 엔티티를 조회한 결과를 반환하는 DTO입니다.
@Getter
public class ResponseBookWithBookInfoDto {
    private Long id;
    private ResponseBookInfoDto bookInfo;
    private boolean isLending;
    private String callSign;
    private BookStatus status;
    private String donator;
    private String createdAt;
    private String updatedAt;

    @Builder
    public ResponseBookWithBookInfoDto(Long id, ResponseBookInfoDto bookInfo,
                                       boolean isLending, String callSign,
                            BookStatus status, String donator, String createdAt, String updatedAt) {
        this.id = id;
        this.bookInfo = bookInfo;
        this.isLending = isLending;
        this.callSign = callSign;
        this.status = status;
        this.donator = donator;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ResponseBookWithBookInfoDto from(Book book) {
        return ResponseBookWithBookInfoDto.builder()
                .id(book.getId())
                .bookInfo(ResponseBookInfoDto.from(book.getBookInfo()))
                .isLending(book.isLendingStatus())
                .callSign(book.getCallSign())
                .status(book.getStatus())
                .donator(book.getDonator())
                .createdAt(book.getCreatedAt().toLocalDate().toString()) // 년-월-일
                .updatedAt(book.getUpdatedAt().toLocalDate().toString())
                .build();
    }
}
