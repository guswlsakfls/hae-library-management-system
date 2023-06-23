package com.hae.library.controller;

import com.hae.library.domain.Book;
import com.hae.library.dto.Book.RequestBookDto;
import com.hae.library.dto.Book.ResponseBookDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.CommonErrorCode;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class BookController {
    private final BookService bookService;

    @PostMapping(value = "/v1/book/create")
    public ResponseResultDto createBook(@RequestBody RequestBookDto requestBookDto) {
        log.error("requestBookDto: {}", requestBookDto.toString());
        bookService.createBook(requestBookDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책이 성공적으로 등록되었습니다")
                .data(null)
                .build();
    }

    @GetMapping(value = "/v1/book/all")
    public ResponseResultDto getAllBook() {
        List<ResponseBookDto> bookList = bookService.getAllBook();
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 목록 조회에 성공하였습니다")
                .data(bookList)
                .build();
    }

    @GetMapping(value = "/v1/book/{bookId}")
    public ResponseResultDto getBookById(@PathVariable("bookId") Long bookId) {
        log.info("bookId: {}", bookId);
        ResponseBookDto responseBookDto = bookService.getBookById(bookId);

        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 조회에 성공하였습니다")
                .data(responseBookDto)
                .build();
    }

    @PutMapping(value = "/v1/book/modify")
    public ResponseResultDto<Object> updateBookById(@RequestBody RequestBookDto requestBookDto) {
        log.info("requestBookDto: {}", requestBookDto.toString());
        Book updateBook = bookService.updateBookById(requestBookDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 정보 수정에 성공하였습니다")
                .data(updateBook)
                .build();
    }

    @DeleteMapping(value = "/v1/book/{bookId}")
    public ResponseResultDto<Object> deleteBookById(@PathVariable("bookId") Long bookId) {
        bookService.deleteBookById(bookId);

        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 정보 삭제에 성공하였습니다")
                .data(null)
                .build();
    }
}
