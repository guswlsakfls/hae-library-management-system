package com.hae.library.dto.BookInfo.Response;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.RequestBook;
import com.hae.library.dto.Category.Response.ResponseCategoryDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// 도서 정보를 반환하는 DTO입니다
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
    private LocalDateTime updatedAt;
    private boolean isApproved;
    private LocalDateTime approvedAt;
    private String requestMemberEmail;

    // 도서 정보를 가지고 도서 요청 정보를 반환할 때 사용합니다.
    public static ResponseBookInfoDto from(BookInfo bookInfo) {
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

    // 도서 요청 정보를 가지고 구매요청 도서 정보를 반환할 때 사용합니다.
    public static ResponseBookInfoDto from(RequestBook requestBook) {
        return ResponseBookInfoDto.builder()
                .id(requestBook.getId())
                .title(requestBook.getBookInfo().getTitle())
                .author(requestBook.getBookInfo().getAuthor())
                .isbn(requestBook.getBookInfo().getIsbn())
                .image(requestBook.getBookInfo().getImage())
                .categoryName(ResponseCategoryDto.from(requestBook.getBookInfo().getCategory()).getCategoryName())
                .publisher(requestBook.getBookInfo().getPublisher())
                .publishedAt(requestBook.getBookInfo().getPublishedAt())
                .isApproved(requestBook.isApproved())
                .createdAt(requestBook.getCreatedAt())
                .updatedAt(requestBook.getUpdatedAt())
                .approvedAt(requestBook.getApprovedAt())
                .requestMemberEmail(requestBook.getMember().getEmail())
                .build();
    }
}
