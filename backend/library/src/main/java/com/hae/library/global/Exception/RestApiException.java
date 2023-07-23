package com.hae.library.global.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 예외를 발생시키는 클래스입니다.
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{
    private final ErrorCode errorCode;
}
