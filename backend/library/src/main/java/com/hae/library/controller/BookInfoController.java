package com.hae.library.controller;

import com.hae.library.domain.BookInfo;
import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.service.BookInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class BookInfoController {
    private final BookInfoService bookInfoService;

    // TODO: 책 청구기호와 같이 저장이 되야 한다
    @PostMapping(value = "/v1/bookinfo")
    public String createBookInfo(RequestBookInfoDto requestBookInfoDto) {
        bookInfoService.createBookInfo(requestBookInfoDto);

        return "BookInfoController";
    }

    @GetMapping(value = "/v1/bookinfo")
    public ResponseResultDto getAllBookInfo() {
        List<BookInfo> bookInfoList = bookInfoService.getAllBookInfo();
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 목록 조회에 성공하였습니다")
                .data(bookInfoList)
                .build();
    }

    @GetMapping(value = "/v1/bookinfo/{bookInfoId}")
    public String getBookInfoById() {
        return "BookInfoController";
    }

    @PutMapping(value = "/v1/bookinfo/{bookInfoId}")
    public String updateBookInfoById() {
        return "BookInfoController";
    }

    @DeleteMapping(value = "/v1/bookinfo/{bookInfoId}")
    public String deleteBookInfoByBookId() {
        return "BookInfoController";
    }
}
