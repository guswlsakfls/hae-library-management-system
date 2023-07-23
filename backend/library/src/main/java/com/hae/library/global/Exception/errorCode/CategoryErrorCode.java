package com.hae.library.global.Exception.errorCode;

import com.hae.library.global.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 도서와 관련된 에러 코드입니다
@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
    // 카테고리
    BAD_REQUEST_CATEGORY(HttpStatus.BAD_REQUEST,"존재하지 않는 카테고리입니다."),
    DUPLICATE_CATEGORY(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다."),
    CATEGORY_IN_USE(HttpStatus.BAD_REQUEST, "카테고리에 책 정보가 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
