package com.hae.library.dto.BookInfo;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.dto.Category.Response.ResponseCategoryDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseBookInfoDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String image;
    private String categoryName; // 그냥 category로 하면 스택오버플로우 난다.
    private int stockQuantity;
    private String publisher;
    private String publishedAt;
    private LocalDateTime createdAt;

    public static ResponseBookInfoDto from(BookInfo bookInfo) {
        // bookInfo가 null인 경우 예외 처리 또는 기본값 설정 등을 수행 // TODO: 수정 필요(필요 없을 듯?)
        if (bookInfo == null) {
            throw new IllegalArgumentException("bookInfo cannot be null");
        }

        return ResponseBookInfoDto.builder()
                .id(bookInfo.getId())
                .title(bookInfo.getTitle())
                .author(bookInfo.getAuthor())
                .isbn(bookInfo.getIsbn())
                .image(bookInfo.getImage())
                .categoryName(ResponseCategoryDto.from(bookInfo.getCategory()).getCategoryName())
                .stockQuantity(bookInfo.getStockQuantity())
                .publisher(bookInfo.getPublisher())
                .publishedAt(bookInfo.getPublishedAt())
                .createdAt(bookInfo.getCreatedAt())
                .build();
    }
}
