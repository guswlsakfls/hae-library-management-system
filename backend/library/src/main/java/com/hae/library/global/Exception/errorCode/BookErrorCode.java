package com.hae.library.global.Exception.errorCode;

import com.hae.library.global.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookErrorCode implements ErrorCode {
    // 도서
    BAD_REQUEST_BOOK(HttpStatus.BAD_REQUEST, "존재하지 않는 책입니다."),
    BAD_REQUEST_BOOKINFO(HttpStatus.BAD_REQUEST, "존재하지 않는 책 정보입니다."),
    NOTHING_REQUEST_INPUT(HttpStatus.BAD_REQUEST, "값을 입력해주세요."),
    DUPLICATE_BOOK(HttpStatus.BAD_REQUEST, "이미 존재하는 책입니다."),
    DUPLICATE_CALLSIGN(HttpStatus.BAD_REQUEST, "이미 존재하는 청구기호입니다."),

    // 대출
    NOT_LENDING_BY_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 대출 정보입니다."),
    BOOK_ALREADY_LENT(HttpStatus.BAD_REQUEST, "이미 대출된 책입니다."),
    BOOK_ALREADY_RETURNED(HttpStatus.BAD_REQUEST, "이미 반납된 책입니다."),
    BOOK_ALREADY_RENEWED(HttpStatus.BAD_REQUEST, "이미 연장된 책입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
