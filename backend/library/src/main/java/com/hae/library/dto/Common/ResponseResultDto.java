package com.hae.library.dto.Common;

import lombok.Builder;
import lombok.Getter;

// 공통 응답 DTO입니다
@Getter
@Builder
public class ResponseResultDto<T> {
    private Integer statusCode;
    private String message;
    private T data;

    @Builder
    public ResponseResultDto(Integer statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
