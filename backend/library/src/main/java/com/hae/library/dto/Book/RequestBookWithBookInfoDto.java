package com.hae.library.dto.Book;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Enum.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RequestBookWithBookInfoDto {
    private Long id;

//    @NotBlank(message = "도서 번호를 입력해주세요.")
//    @Pattern(regexp = "^[A-Z]{2}[0-9]{4}$", message = "도서 번호는 2자의 대문자와 4자의 숫자로 이루어져야 합니다.")
    private String callSign;

//    @Pattern(regexp = "^[0-9]{13}$", message = "ISBN은 13자의 숫자로 이루어져야 합니다.")
//    @NotBlank(message = "ISBN을 입력해주세요.")
    private String isbn;

//    @Size(max = 200, message = "도서 제목은 200자를 넘을 수 없습니다.")
//    @NotBlank(message = "도서 제목을 입력해주세요.")
    private String title;

//    @Size(max = 100, message = "저자는 100자를 넘을 수 없습니다.")
//    @NotBlank(message = "저자를 입력해주세요.")
    private String author;

//    @Size(max = 20, message = "출판사는 20자를 넘을 수 없습니다.")
//    @NotBlank(message = "출판사를 입력해주세요.")
    private String publisher;

    private String image;

//    @Size(max = 10, message = "출판일은 10자를 넘을 수 없습니다.")
//    @NotBlank(message = "출판일을 입력해주세요.")
    private String publishedAt;

    private String status;

//    @Size(max = 20, message = "기증자는 20자를 넘을 수 없습니다.")
    private String donator;

    @Builder
    public RequestBookWithBookInfoDto(String callSign, String isbn, String title,
                           String author, String publisher,
                          String image, String publishedAt, String status, String donator) {
        this.callSign = callSign;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.publishedAt = publishedAt;
        this.status = status;
        this.donator = donator;
    }
}

