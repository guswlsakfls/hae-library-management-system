package com.hae.library.global.Exception;

import com.hae.library.global.Exception.errorCode.CommonErrorCode;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.validation.BindException;
import java.util.List;
import java.util.stream.Collectors;

// 전역으로 발생하는 예외를 처리하는 클래스입니다.
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // RestApiException 타입의 예외를 처리합니다.
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    // IllegalArgumentException 타입의 예외를 처리합니다.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.info("IllegalArgumentException");
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    // MethodArgumentNotValidException 타입의 예외를 처리합니다.
    // Bean Validation 어노테이션(@NotNull, @Min, @Max, @Size 등)에 따른 유효성 검사에 실패했을 때 발생합니다.
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValid (
            final MethodArgumentNotValidException e) {
        log.info("MethodArgumentNotValidException");
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, errorCode);
    }

    // ConstraintViolationException 타입의 예외를 처리합니다.
    // @NotNull, @Size, @Min, @Max 등의 어노테이션을 필드, jpa @Column에 사용했을 때 발생합니다.
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleMethodConstraintViolationException(ConstraintViolationException e) {
        log.info("ConstraintViolationException");
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(e, errorCode);
    }

    // 모든 타입의 예외를 처리합니다. 이 메소드는 다른 메소드에서 처리하지 못한 예외를 처리합니다.
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.info("Exception : {}", ex.getMessage());
        ErrorCode errorCode = CommonErrorCode.RESOURCE_NOT_FOUND;
        return handleExceptionInternal(errorCode);
    }


    // ConstraintViolationException 타입의 예외를 처리하는 메소드입니다.
    private ResponseEntity<Object> handleExceptionInternal(ConstraintViolationException e, ErrorCode errorCode) {
        String error = e.getConstraintViolations().iterator().next().getMessage();
        log.info("ConstraintViolationException : {}", error);

        return ResponseEntity.status(errorCode.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(makeErrorResponse(errorCode, error));
    }



    // ErrorCode를 이용해 ResponseEntity를 생성합니다.
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(makeErrorResponse(errorCode));
    }

    // ErrorCode로부터 ErrorResponse 객체를 생성합니다.
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        log.info("ErrorResponse : {}", errorCode.getMessage());
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    // ErrorCode와 메세지를 이용해 ResponseEntity를 생성합니다.
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(makeErrorResponse(errorCode, message));
    }

    // ErrorCode와 메세지로부터 ErrorResponse 객체를 생성합니다.
    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        log.info("ErrorResponse : {}", errorCode.getMessage());
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    // BindException을 처리하고 그 결과를 담은 ResponseEntity를 생성합니다.
    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        log.info("BindException : {}", errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(makeErrorResponse(e, errorCode));
    }

    // BindException을 처리하고 그 결과를 담은 ErrorResponse를 생성합니다.
    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        log.info("ErrorResponse : {}", errorCode.getMessage());
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }
}