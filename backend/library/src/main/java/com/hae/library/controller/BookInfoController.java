package com.hae.library.controller;

import com.hae.library.config.Security.RoleInterface;
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
        log.info("모든 책 정보 조회: [GET] /bookinfo/all - 검색: {}, 페이지: {}, 사이즈: {}", search, page, size);
        // 검색 키워드와 페이지네이션 정보를 인자로 주어 책 정보를 가져옵니다.
        Page<ResponseBookInfoDto> responseBookInfoDtoList =
                bookInfoService.getAllBookInfo(search, page, size);

        // 책 정보 리스트 와 페이지 네이션 정보를 데이터로 설정합니다.
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("bookInfoList", responseBookInfoDtoList.getContent());
        responseData.put("totalElements", responseBookInfoDtoList.getTotalElements());
        responseData.put("currentPage", responseBookInfoDtoList.getNumber());
        responseData.put("size", responseBookInfoDtoList.getSize());

        log.info("모든 책 정보 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("모든 책 정보 조회에 성공하였습니다")
                .data(responseData)
                .build();
    }

    // 책 정보를 조회하는 요청을 처리합니다.
    @GetMapping(value = "/bookinfo/{bookInfoId}")
    public ResponseResultDto<Object> getBookInfoById(@PathVariable(required = false) Long bookInfoId) {
        log.info("책 정보 조회: [GET] /bookinfo/{} - ID로 책 정보 조회", bookInfoId);
        ResponseBookInfoWithBookDto responseBookInfoDto = bookInfoService.getBookInfoById(bookInfoId);

        log.info("ID로 책 정보 조회에 성공하였습니다 {}", bookInfoId);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 한 개 정보 조회에 성공하였습니다")
                .data(responseBookInfoDto)
                .build();
    }

    // ISBN에 해당하는 책 정보를 조회하는 요청을 처리합니다.
//    @RoleInterface.AdminAuthorize
    @GetMapping(value = {"/bookinfo/isbn/" ,"/bookinfo/isbn/{isbn}"})
    public ResponseResultDto<Object> getBookInfoByIsbn(@PathVariable(required = false) String isbn) {
        log.info("책 정보 조회: /bookinfo/isbn/{} - ISBN으로 책 정보 조회", isbn);
        ResponseBookInfoWithBookDto responseBookInfoDto = bookInfoService.getBookInfoByIsbn(isbn);

        log.info("ISBN으로 책 정보 조회에 성공하였습니다 {}", isbn);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("하나의 책 정보 조회에 성공하였습니다")
                .data(responseBookInfoDto)
                .build();
    }

    // 특정 책 정보를 삭제하는 요청을 처리합니다.
    @DeleteMapping(value = "/bookinfo/{bookInfoId}/delete")
    public ResponseResultDto<Object> deleteBookInfoById(@PathVariable Long bookInfoId) {
        log.info("책 삭제: [DELETE] /bookinfo/{}/delete - ID로 책 정보 삭제", bookInfoId);
        bookInfoService.deleteBookInfoById(bookInfoId);

        log.info("ID로 책 정보 삭제에 성공하였습니다 {}", bookInfoId);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 정보 삭제에 성공하였습니다")
                .data(null)
                .build();
    }
}
