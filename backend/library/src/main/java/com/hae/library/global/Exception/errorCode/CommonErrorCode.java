package com.hae.library.global.Exception.errorCode;

import com.hae.library.global.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 공통 에러 코드입니다
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "옳지 않은 변수 요청입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 버튼을 실행할 수 없습니다, 다시한번 시도해 주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "오류가 발생했습니다 다시한번 시도해 주세요."),
    JSON_PROCESSING_EXCEPTION(HttpStatus.BAD_REQUEST, "JSON 처리 중 오류가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

