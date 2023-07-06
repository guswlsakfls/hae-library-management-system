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
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
@Validated
public class BookController {
    private final BookService bookService;


    // 새 책을 생성합니다
    @PostMapping(value = "/admin/book/create")
    public ResponseResultDto createBook(@RequestBody @Valid RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        log.info("책 생성 요청: [POST] /book/create - {}", requestBookWithBookInfoDto.toString());
        ResponseBookWithBookInfoDto responseBookWithBookInfoDto = bookService.createBook(requestBookWithBookInfoDto);

        log.info("책이 성공적으로 등록되었습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책이 성공적으로 등록되었습니다")
                .data(responseBookWithBookInfoDto)
                .build();
    }

    // 모든 책을 검색하거나, 검색어에 해당하는 책을 검색합니다
    @GetMapping(value = "/admin/book/all")
    public ResponseResultDto getAllBook(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ) {
        log.info("모든 책 정보 조회: [GET] /book/all - 검색: {}, 페이지: {}, 사이즈: {}", search, page, size);
        Page<ResponseBookWithBookInfoDto> responseBookList = bookService.getAllBook(search, page,
                size);

        // 책 정보 리스트 와 페이지 네이션 정보를 데이터로 설정합니다.
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("bookList", responseBookList.getContent());
        responseData.put("totalElements", responseBookList.getTotalElements());
        responseData.put("currentPage", responseBookList.getNumber());
        responseData.put("size", responseBookList.getSize());

        log.info("모든 책 정보 조회에 성공하였습니다.");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("모든 책 조회에 성공하였습니다")
                .data(responseData)
                .build();
    }


    // 지정된 ID를 가진 책을 검색합니다
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

    // 청구기호로 책을 검색합니다
    @GetMapping(value = "/admin/book/callsign")
    public ResponseResultDto getBookByCallSign(@RequestParam("callsign") @NotBlank(message =
            "청구기호를 입력해 주세요.") String callSign) {
        log.info("청구 기호로 책 조회 요청: [GET] /book/callsign - 청구 기호 {}", callSign);
        ResponseBookWithBookInfoDto bookWithBookInfoDto = bookService.getBookByCallSign(callSign);

        log.info("청구 기호로 책 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("하나의 책 조회에 성공하였습니다")
                .data(bookWithBookInfoDto)
                .build();
    }

    // 책 정보를 업데이트 합니다
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

    // 지정된 ID를 가진 책을 삭제합니다
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
