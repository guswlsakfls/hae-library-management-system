package com.hae.library.controller;

import com.hae.library.dto.BookInfo.Request.RequestIsbnDto;
import com.hae.library.dto.BookInfo.Response.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.Response.ResponseBookInfoWithBookDto;
import com.hae.library.dto.Common.ResponseResultDto;
import com.hae.library.service.BookInfoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class BookInfoController {
    private final BookInfoService bookInfoService;

    /**
     * 검색어를 통해 책 리스트를 가져옵니다.
     *
     * @param search
     * @param page
     * @param size
     * @param category
     * @param sort
     * @return 모든 책 정보 리스트
     */
    @GetMapping(value = "/bookinfo/all")
    public ResponseResultDto getAllBookInfoByOptions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort
    ) {
        log.info("모든 책 정보 조회: [GET] /bookinfo/all - 검색: {}, 페이지: {}, 사이즈: {}, 카테고리: {}, 정렬: {}",
                search,
                page,
                size,
                category,
                sort);
        // 검색 키워드와 페이지네이션 정보를 인자로 주어 책 정보를 가져옵니다.
        Page<ResponseBookInfoDto> responseBookInfoDtoList =
                bookInfoService.getAllBookInfo(search, page, size, category, sort);

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

    /**
     * id로 책 정보를 등록합니다.
     *
     * @param bookInfoId
     * @return 책 정보
     */
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

    /**
     * isbn으로 책 정보를 등록합니다.
     *
     * @param requestIsbnDto
     * @return 책 정보
     */
    @GetMapping(value = "/admin/bookinfo/isbn")
    public ResponseResultDto<Object> getBookInfoByIsbn(@Valid RequestIsbnDto requestIsbnDto) {
        log.info("책 정보 조회: /bookinfo/isbn - {} - ISBN으로 책 정보 조회", requestIsbnDto.getIsbn());
        ResponseBookInfoWithBookDto responseBookInfoDto = bookInfoService.getBookInfoByIsbn(requestIsbnDto);

        log.info("ISBN으로 책 정보 조회에 성공하였습니다 {}", requestIsbnDto.getIsbn());
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("하나의 책 정보 조회에 성공하였습니다")
                .data(responseBookInfoDto)
                .build();
    }

    /**
     * id로 책 정보를 삭제합니다.
     *
     * @param bookInfoId
     * @return "책 정보 삭제에 성공하였습니다" 메시지
     */
    @DeleteMapping(value = "/admin/bookinfo/{bookInfoId}")
    public ResponseResultDto<Object> deleteBookInfoById(@PathVariable Long bookInfoId) {
        log.info("책 삭제: [DELETE] /bookinfo/{} - ID로 책 정보 삭제", bookInfoId);
        bookInfoService.deleteBookInfoById(bookInfoId);

        log.info("ID로 책 정보 삭제에 성공하였습니다 {}", bookInfoId);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 정보 삭제에 성공하였습니다")
                .build();
    }
}
