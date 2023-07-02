package com.hae.library.dto.Book;

import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.Lending.ResponseLendingDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
        boolean isLending = false;
        if (book.getLending() != null) {
            isLending = true;
        }
        return ResponseBookWithBookInfoDto.builder()
                .id(book.getId())
                .bookInfo(ResponseBookInfoDto.from(book.getBookInfo()))
                .isLending(isLending)
                .callSign(book.getCallSign())
                .status(book.getStatus())
                .donator(book.getDonator())
                .createdAt(book.getCreatedAt().toLocalDate().toString()) // 년-월-일
                .updatedAt(book.getUpdatedAt().toLocalDate().toString())
                .build();
    }
}
