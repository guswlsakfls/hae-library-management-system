package com.hae.library.global.Exception.errorCode;

import com.hae.library.global.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookErrorCode implements ErrorCode {
    BAD_REQUEST_BOOK(HttpStatus.BAD_REQUEST, "존재하지 않는 책입니다."),
    BAD_REQUEST_BOOKINFO_BY_ID(HttpStatus.BAD_REQUEST, "존재하지 않는 책 정보입니다."),
    DUPLICATE_BOOK(HttpStatus.BAD_REQUEST, "이미 존재하는 책입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
