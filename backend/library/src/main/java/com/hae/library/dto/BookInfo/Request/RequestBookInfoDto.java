package com.hae.library.dto.BookInfo.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 책 정보에 대한 요청 DTO입니다
@Getter
@Setter
@NoArgsConstructor
public class RequestBookInfoDto {
    private Long id;

    @Pattern(regexp = "^[0-9]{13}$", message = "ISBN은 9~13자의 숫자로 이루어져야 합니다.")
    private String isbn;

    @Size(max = 300, message = "도서 제목은 300자를 넘을 수 없습니다.")
    @NotBlank(message = "도서 제목을 입력해주세요.")
    private String title;

    @Size(max = 300, message = "저자는 300자를 넘을 수 없습니다.")
    @NotBlank(message = "저자를 입력해주세요.")
    private String author;

    @Size(max = 50, message = "출판사는 50자를 넘을 수 없습니다.")
    @NotBlank(message = "출판사를 입력해주세요.")
    private String publisher;

    @Size(max = 100, message = "이미지url은 100자를 넘을 수 없습니다.")
    @NotBlank(message = "이미지를 입력해주세요.")
    private String image;

    @Size(max = 20, message = "출판일은 10자를 넘을 수 없습니다.")
    @NotBlank(message = "출판일을 입력해주세요.")
    private String publishedAt;

    @Size(max = 10, message = "도서 카테고리는 10자를 넘을 수 없습니다.")
    @NotBlank(message = "도서 카테고리를 입력해주세요.")
    private String categoryName;

    @Builder
    public RequestBookInfoDto(Long id, String isbn, String title, String author,
                              String publisher,
                              String image, String publishedAt, String categoryName) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.publishedAt = publishedAt;
        this.categoryName = categoryName;

    }
}
