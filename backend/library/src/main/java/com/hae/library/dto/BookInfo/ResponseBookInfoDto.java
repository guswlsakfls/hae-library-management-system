package com.hae.library.dto.BookInfo;

import com.hae.library.domain.BookInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseBookInfoDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String image;
    private String publisher;
    private String publishedAt;

    public static ResponseBookInfoDto from(BookInfo bookInfo) {
        if (bookInfo == null) {
            // bookInfo가 null인 경우 예외 처리 또는 기본값 설정 등을 수행
            throw new IllegalArgumentException("bookInfo cannot be null");
        }

        return ResponseBookInfoDto.builder()
                .id(bookInfo.getId())
                .title(bookInfo.getTitle())
                .author(bookInfo.getAuthor())
                .isbn(bookInfo.getIsbn())
                .image(bookInfo.getImage())
                .publisher(bookInfo.getPublisher())
                .publishedAt(bookInfo.getPublishedAt())
                .build();
    }
}
