package com.hae.library.controller;

import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.service.BookInfoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class BookInfoController {
    private final BookInfoService bookInfoService;

    @PostMapping(value = "/v1/bookinfo")
    public String createBookInfo(RequestBookInfoDto requestBookInfoDto) {
        bookInfoService.createBookInfo(requestBookInfoDto);

        return "BookInfoController";
    }

    @GetMapping(value = "/v1/bookinfo")
    public String getAllBookInfo() {
        return "BookInfoController";
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
