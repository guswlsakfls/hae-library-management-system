package com.hae.library.dto.BookInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestBookInfoDto {
    private Long id;
    private Long categoryId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String image;
    private String publishedAt;

    @Builder
    public RequestBookInfoDto(Long id, Long categoryId, String isbn, String title, String author,
                              String publisher,
                              String image, String publishedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.publishedAt = publishedAt;
    }
}
