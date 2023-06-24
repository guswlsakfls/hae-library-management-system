package com.hae.library.dto.Book;

import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseBookWithBookInfoDto {
    private Long id;
    private ResponseBookInfoDto bookInfo;
    private String callSign;
    private BookStatus status;
    private String donator;

    @Builder
    public ResponseBookWithBookInfoDto(Long id, ResponseBookInfoDto bookInfo, String callSign,
                            BookStatus status, String donator) {
        this.id = id;
        this.bookInfo = bookInfo;
        this.callSign = callSign;
        this.status = status;
        this.donator = donator;
    }

    public static ResponseBookWithBookInfoDto from(Book book) {
        return ResponseBookWithBookInfoDto.builder()
                .id(book.getId())
                .bookInfo(ResponseBookInfoDto.from(book.getBookInfo()))
                .callSign(book.getCallSign())
                .status(book.getStatus())
                .donator(book.getDonator())
                .build();
    }
}
