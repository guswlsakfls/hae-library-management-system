package com.hae.library.global.Exception.errorCode;

import com.hae.library.global.Exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    // 로그인
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다"),
    ADMIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "관리자를 찾을 수 없습니다"),
    MEMBER_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 회원입니다"),
    INACTIVE_MEMBER(HttpStatus.FORBIDDEN, "회원은 활동이 중단되었습니다"),

    // 회원가입
    MEMBER_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    MEMBER_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다"),

    // 대출
    USER_NOT_LENDING_AVAILABLE(HttpStatus.BAD_REQUEST, "3권까지 대출 가능합니다"),
    USER_OVERDUE(HttpStatus.BAD_REQUEST, "연체일이 지난 후에 대출이 가능합니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
