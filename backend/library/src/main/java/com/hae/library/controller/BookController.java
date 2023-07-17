package com.hae.library.controller;

import com.hae.library.dto.Book.Request.RequestBookWithBookInfoDto;
import com.hae.library.dto.Book.Response.ResponseBookWithBookInfoDto;
import com.hae.library.dto.Common.ResponseResultDto;
import com.hae.library.dto.Lending.Request.RequestCallsignDto;
import com.hae.library.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
@Validated
public class BookController {
    private final BookService bookService;

    /**
     * 책 추가 요청 정보를 받아 저장합니다.
     *
     * @param requestBookWithBookInfoDto
     * @return "도서를 성공적으로 등록하였습니다" 메시지
     */
    @PostMapping(value = "/admin/book/create")
    public ResponseResultDto createBook(@RequestBody @Valid RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        log.info("도서 추가 요청: [POST] /book/create - {}", requestBookWithBookInfoDto.toString());
        bookService.createBook(requestBookWithBookInfoDto);

        log.info("도서를 성공적으로 등록하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("도서를 성공적으로 등록하였습니다")
                .build();
    }

    /**
     * 책 ID로 책을 조회합니다.
     *
     * @param bookId
     * @return 책 정보
     */
    @GetMapping(value = "/member/book/{bookId}/info")
    public ResponseResultDto getBookById(@PathVariable("bookId") @Positive Long bookId) {
        log.info("ID로 책 조회 요청: [GET] /book/{bookId}/info - 책 ID {}", bookId);
        ResponseBookWithBookInfoDto  bookWithBookInfoDto = bookService.getBookById(bookId);

        log.info("ID로 책 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("하나의 책 조회에 성공하였습니다")
                .data(bookWithBookInfoDto)
                .build();
    }

    /**
     * 책 제목으로 책을 조회합니다.
     *
     * @param requestCallsignDto
     * @return 책 정보
     */
    @GetMapping(value = "/admin/book/callsign")
    public ResponseResultDto getBookByCallSign(@Valid RequestCallsignDto requestCallsignDto) {
        log.info("청구 기호로 책 조회 요청: [GET] /book/callsign - 청구 기호 {}", requestCallsignDto);
        ResponseBookWithBookInfoDto bookWithBookInfoDto = bookService.getBookByCallSign(requestCallsignDto);

        log.info("청구 기호로 책 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("하나의 책 조회에 성공하였습니다")
                .data(bookWithBookInfoDto)
                .build();
    }

    /**
     * 책 정보를 수정합니다.
     *
     * @param requestBookWithBookInfoDto
     * @return 수정된 책 정보
     */
    @PutMapping(value = "/admin/book/update")
    public ResponseResultDto<Object> updateBook(@RequestBody @Valid RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        log.info("책 정보 업데이트 요청: [PUT] /book/update - {}", requestBookWithBookInfoDto.toString());
        ResponseBookWithBookInfoDto bookWithBookInfo = bookService.updateBook(requestBookWithBookInfoDto);

        log.info("책 정보 업데이트에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 수정에 성공하였습니다")
                .data(bookWithBookInfo)
                .build();
    }

    /**
     * 책 ID로 책을 삭제합니다.
     *
     * @param bookId
     * @return "책 삭제에 성공하였습니다" 메시지
     */
    @DeleteMapping(value = "/admin/book/{bookId}/delete")
    public ResponseResultDto<Object> deleteBookById(@PathVariable("bookId") Long bookId) {
        log.info("책 삭제 요청: [DELETE] /book/{bookId}/delete - 책 ID {}", bookId);
        bookService.deleteBookById(bookId);

        log.info("책 삭제에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 삭제에 성공하였습니다")
                .build();
    }
}
