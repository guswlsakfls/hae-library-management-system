package com.hae.library.controller;

import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.service.BookInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class BookInfoController {
    private final BookInfoService bookInfoService;

    // 모든 책 정보를 조회하는 요청을 처리합니다.
    @GetMapping(value = "/bookinfo/all")
    public ResponseResultDto getAllBookInfoByOptions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ) {
        log.info("API called: /bookinfo/all with parameters - search: {}, page: {}, size: {}", search, page, size);
        // 검색 키워드와 페이지네이션 정보를 인자로 주어 책 정보를 가져옵니다.
        Page<ResponseBookInfoDto> responseBookInfoDtoList =
                bookInfoService.getAllBookInfo(search, page, size);

        log.error("responseBookInfoDtoList: {}", responseBookInfoDtoList.toString());
        // 책 정보 리스트 와 페이지 네이션 정보를 데이터로 설정합니다.
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("bookInfoList", responseBookInfoDtoList.getContent());
        responseData.put("totalElements", responseBookInfoDtoList.getTotalElements());
        responseData.put("currentPage", responseBookInfoDtoList.getNumber());
        responseData.put("size", responseBookInfoDtoList.getSize());

        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())  // HTTP 상태 코드 200을 설정
                .message("모든 책 정보 조회에 성공하였습니다")  // 메시지 설정
                .data(responseData)  // 책 정보 목록을 데이터로 설정
                .build();
    }

    // 하나의 책 정보를 조회하는 요청을 처리합니다.
    @GetMapping(value = "/bookinfo/{bookInfoId}")
    public ResponseResultDto<Object> getBookInfoById(@PathVariable Long bookInfoId) {
        // 특정 책 정보를 보유도서와 함께 가져옵니다.
        ResponseBookInfoWithBookDto responseBookInfoDto = bookInfoService.getBookInfoById(bookInfoId);
        // HTTP 상태 코드, 메시지, 데이터를 포함하는 응답 DTO를 반환합니다.
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())  // HTTP 상태 코드 200을 설정
                .message("책 한 개 정보 조회에 성공하였습니다")  // 메시지 설정
                .data(responseBookInfoDto)  // 특정 책 정보를 데이터로 설정
                .build();
    }

    // ISBN에 해당하는 책 정보를 조회하는 요청을 처리합니다.
    @GetMapping(value = "/bookinfo/isbn/{isbn}")
    public ResponseResultDto<Object> getBookInfoByIsbn(@PathVariable String isbn) {
        // 특정 책 정보를 보유도서와 함께 가져옵니다.
        ResponseBookInfoWithBookDto responseBookInfoDto = bookInfoService.getBookInfoByIsbn(isbn);
        // HTTP 상태 코드, 메시지, 데이터를 포함하는 응답 DTO를 반환합니다.
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())  // HTTP 상태 코드 200을 설정
                .message("하나의 책 정보 조회에 성공하였습니다")  // 메시지 설정
                .data(responseBookInfoDto)  // 특정 책 정보를 데이터로 설정
                .build();
    }

    // 특정 책 정보를 삭제하는 요청을 처리합니다.
    @DeleteMapping(value = "/bookinfo/{bookInfoId}/delete")
    public ResponseResultDto<Object> deleteBookInfoById(@PathVariable Long bookInfoId) {
        // 특정 책 정보를 삭제하고
        bookInfoService.deleteBookInfoById(bookInfoId);
        // HTTP 상태 코드와 메시지를 포함하는 응답 DTO를 반환합니다. 데이터는 없습니다.
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())  // HTTP 상태 코드 200을 설정
                .message("책 정보 삭제에 성공하였습니다")  // 메시지 설정
                .data(null)  // 데이터는 없음
                .build();
    }
}
