package com.hae.library.controller;

import com.hae.library.dto.BookInfo.Request.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.Response.ResponseBookInfoDto;
import com.hae.library.dto.Common.ResponseResultDto;
import com.hae.library.service.RequestBookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class RequestBookController {
    private final RequestBookService requestBookService;

    // 도서 구매 요청들을 추가합니다.
    @PostMapping(value = "/admin/request-book/create")
    public ResponseResultDto<Object> createRequestBook(@RequestBody @Valid RequestBookInfoDto requestBookInfoDto) {
        log.info("도서 구매 요청: [POST] /request-book/create");
        requestBookService.createRequestBook(requestBookInfoDto);

        log.info("도서 구매 요청에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(200)
                .message("도서 구매 요청에 성공하였습니다")
                .build();
    }

    // 도서 구매 요청들을 조회합니다.
    @GetMapping(value = "/admin/request-book/all")
    public ResponseResultDto<Object> getRequestBook(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String approved
            ) {
        log.info("도서 구매 요청 조회: [GET] /request-book - 검색: {}, 페이지: {}, 사이즈: {}, 카테고리: {}, 정렬: {}, 승인: {}",
                search,
                page,
                size,
                category,
                sort,
                approved
        );

        // 구매 요청 도서 정보 리스트 와 페이지 네이션 정보를 가져옵니다.
        Page<ResponseBookInfoDto> responseBookDtoList =
                requestBookService.getRequestBookList(search, page, size, category, sort, approved);

        // 책 정보 리스트 와 페이지 네이션 정보를 데이터로 설정합니다.
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("requestBookInfoList", responseBookDtoList.getContent());
        responseData.put("totalElements", responseBookDtoList.getTotalElements());
        responseData.put("currentPage", responseBookDtoList.getNumber());
        responseData.put("size", responseBookDtoList.getSize());

        log.info("도서 구매 요청 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(200)
                .message("도서 구매 요청 조회에 성공하였습니다")
                .data(responseData)
                .build();
    }

    // 도서 구매 요청을 삭제합니다.
    @DeleteMapping(value = "/admin/request-book/{requestBookId}/delete")
    public ResponseResultDto<Object> deleteRequestBook(@PathVariable Long requestBookId) {
        log.info("도서 구매 요청 삭제: [DELETE] /request-book/delete/{}", requestBookId);
        requestBookService.deleteRequestBook(requestBookId);

        log.info("도서 구매 요청 삭제에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(200)
                .message("도서 구매 요청 삭제에 성공하였습니다")
                .build();
    }
}
