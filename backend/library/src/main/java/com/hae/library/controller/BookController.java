package com.hae.library.controller;

import com.hae.library.domain.Book;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.Book.ResponseBookWithBookInfoDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.CommonErrorCode;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
@Validated
public class BookController {
    private final BookService bookService;

    @ResponseBody
    @PostMapping(value = "/book/create")
    public ResponseResultDto createBook(@RequestBody @Valid RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        log.error("requestBookWithBookInfoDto: {}", requestBookWithBookInfoDto.toString());
        ResponseBookWithBookInfoDto responseBookWithBookInfoDto = bookService.createBook(requestBookWithBookInfoDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책이 성공적으로 등록되었습니다")
                .data(responseBookWithBookInfoDto)
                .build();
    }

    @GetMapping(value = "/book/all")
    public ResponseResultDto getAllBook() {
        List<ResponseBookWithBookInfoDto> bookList = bookService.getAllBook();
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("모든 책 조회에 성공하였습니다")
                .data(bookList)
                .build();
    }

    @GetMapping(value = "/book/{bookId}/info")
    public ResponseResultDto getBookById(@PathVariable("bookId") @Positive Long bookId) {
        log.info("bookId: {}", bookId);
        ResponseBookWithBookInfoDto  bookWithBookInfoDto = bookService.getBookById(bookId);

        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("하나의 책 조회에 성공하였습니다")
                .data(bookWithBookInfoDto)
                .build();
    }

    @GetMapping(value = "/book/callsign")
    public ResponseResultDto getBookByCallSign(@RequestParam("callsign") @NotBlank(message =
            "청구기호를 입력해 주세요.") String callSign) {
        log.info("callSign: {}", callSign);
        ResponseBookWithBookInfoDto bookWithBookInfoDto = bookService.getBookByCallSign(callSign);

        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("하나의 책 조회에 성공하였습니다")
                .data(bookWithBookInfoDto)
                .build();
    }

    @PutMapping(value = "/book/modify")
    public ResponseResultDto<Object> updateBook(@RequestBody @Valid RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        log.info("requestBookWithBookInfoDto: {}", requestBookWithBookInfoDto.toString());
        ResponseBookWithBookInfoDto bookWithBookInfo = bookService.updateBook(requestBookWithBookInfoDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 수정에 성공하였습니다")
                .data(bookWithBookInfo)
                .build();
    }

    @DeleteMapping(value = "/book/{bookId}/delete")
    public ResponseResultDto<Object> deleteBookById(@PathVariable("bookId") Long bookId) {
        bookService.deleteBookById(bookId);

        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 삭제에 성공하였습니다")
                .data(null)
                .build();
    }
}
