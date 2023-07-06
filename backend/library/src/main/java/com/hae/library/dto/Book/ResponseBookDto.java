package com.hae.library.dto.Book;

import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseBookDto {
    private Long id;
    private String callSign;
    private BookStatus status;
    private String donator;
    private Boolean isAvailable;

    @Builder
    public ResponseBookDto(Long id, String callSign,
                                       BookStatus status, String donator, Boolean isAvailable) {
        this.id = id;
        this.callSign = callSign;
        this.status = status;
        this.donator = donator;
        this.isAvailable = isAvailable;
    }

    public static ResponseBookDto from(Book book) {
        return ResponseBookDto.builder()
                .id(book.getId())
                .callSign(book.getCallSign())
                .status(book.getStatus())
                .donator(book.getDonator())
                .isAvailable(book.isLendingStatus())
                .build();
    }
}
