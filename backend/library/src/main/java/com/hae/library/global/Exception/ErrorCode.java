package com.hae.library.global.Exception;

import org.springframework.http.HttpStatus;

// 에러 코드 인터페이스입니다
public interface ErrorCode {
    String name();
    HttpStatus getHttpStatus();
    String getMessage();
}
