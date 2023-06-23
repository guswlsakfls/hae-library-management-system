package com.hae.library.dto;

import lombok.Builder;
import lombok.Getter;

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
