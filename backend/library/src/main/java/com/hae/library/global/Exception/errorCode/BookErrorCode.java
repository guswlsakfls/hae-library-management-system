package com.hae.library.global.Exception.errorCode;

import com.hae.library.global.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookErrorCode implements ErrorCode {
    NOT_FOUND_BOOK(HttpStatus.NOT_FOUND, "존재하지 않는 책입니다."),
    DUPLICATE_BOOK(HttpStatus.BAD_REQUEST, "이미 존재하는 책입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
