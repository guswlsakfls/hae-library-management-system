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
