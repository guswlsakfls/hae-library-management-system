package com.hae.library.dto.Book.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class RequestBookWithBookInfoDto {
    private Long id;

    // TODO: 각 변수에 맞는 validation 추가해야한다.
    @NotBlank(message = "청구기호를 입력해주세요.")
    @Size(max = 20, message = "청구기호는 20자를 넘을 수 없습니다.")
    @Pattern(regexp = ".*\\.c\\d+$", message = "올바른 청구기호 형식이 아닙니다. .c 다음에 숫자를 입력해주세요.")
    private String callSign;

    @Pattern(regexp = "^[0-9]{13}$", message = "ISBN은 13자의 숫자로 이루어져야 합니다.")
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

    @Size(max = 10, message = "도서 상태는 10자를 넘을 수 없습니다.")
    @NotBlank(message = "도서 상태를 입력해주세요.")
    private String status;

    @Size(max = 100, message = "기증자는 100자를 넘을 수 없습니다.")
    private String donator;

    @Builder
    public RequestBookWithBookInfoDto(Long id, String callSign, String isbn, String title,
                           String author, String publisher, String categoryName,
                          String image, String publishedAt, String status, String donator) {
        this.id = id;
        this.callSign = callSign;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.publishedAt = publishedAt;
        this.categoryName = categoryName;
        this.status = status;
        this.donator = donator;
    }

    /**
     * 청구기호를 업데이트 한다.
     * @param callSign
     */
    public void updateCallSign(String callSign) {
        this.callSign = callSign;
    }
}

