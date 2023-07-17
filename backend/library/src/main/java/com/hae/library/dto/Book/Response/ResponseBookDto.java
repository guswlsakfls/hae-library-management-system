package com.hae.library.dto.Book.Response;

import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseBookDto {
    private Long id;
    private String callSign;
    private BookStatus status;
    private String donator;
    private Boolean isAvailable;
    private String createdAt;
    private String updatedAt;

    @Builder
    public ResponseBookDto(Long id, String callSign,
                                       BookStatus status, String donator, Boolean isAvailable, String createdAt, String updatedAt) {
        this.id = id;
        this.callSign = callSign;
        this.status = status;
        this.donator = donator;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ResponseBookDto from(Book book) {
        return ResponseBookDto.builder()
                .id(book.getId())
                .callSign(book.getCallSign())
                .status(book.getStatus())
                .donator(book.getDonator())
                .isAvailable(book.isLendingStatus())
                .createdAt(book.getCreatedAt().toLocalDate().toString())
                .updatedAt(book.getUpdatedAt().toLocalDate().toString())
                .build();
    }
}
