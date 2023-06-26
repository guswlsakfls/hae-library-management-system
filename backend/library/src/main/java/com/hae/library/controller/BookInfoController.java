package com.hae.library.controller;

import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.service.BookInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class BookInfoController {
    private final BookInfoService bookInfoService;

//    // TODO: 실물 책을 가지고 하는 것이라면, 실물 책이 있을시에만 책 정보를 등록한다.(bookContoller에서 bookInfo를 등록한다.)
//    @PostMapping(value = "/bookinfo")
//    public String createBookInfo(RequestBookInfoDto requestBookInfoDto) {
//        bookInfoService.createBookInfo(requestBookInfoDto);
//
//        return "BookInfoController";
//    }

    @GetMapping(value = "/bookinfo/all")
    public ResponseResultDto getAllBookInfo() {
        List<ResponseBookInfoDto> responseBookInfoDtoList = bookInfoService.getAllBookInfo();
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("모든 책 정보 조회에 성공하였습니다")
                .data(responseBookInfoDtoList)
                .build();
    }

    @GetMapping(value = "/bookinfo/{bookInfoId}")
    public ResponseResultDto<Object> getBookInfoById(@PathVariable Long bookInfoId) {
        ResponseBookInfoWithBookDto responseBookInfoDto = bookInfoService.getBookInfoById(bookInfoId);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("하나의 책 정보 조회에 성공하였습니다")
                .data(responseBookInfoDto)
                .build();
    }

    // TODO: 실물 책 정보까지 한번에 수정(BookController에서 bookInfo를 수정한다. / 추후 고민)
//    @PutMapping(value = "/bookinfo/modify")
//    public ResponseResultDto<Object> updateBookInfoById(@RequestBody RequestBookInfoDto requestBookInfoDto) {
//        log.info("requestBookInfoDto: {}", requestBookInfoDto.toString());
//        ResponseBookInfoDto responseBookInfoDto = bookInfoService.updateBookInfoById(requestBookInfoDto);
//        return ResponseResultDto.builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("책 정보 수정에 성공하였습니다")
//                .data(responseBookInfoDto)
//                .build();
//    }

    @DeleteMapping(value = "/bookinfo/{bookInfoId}/delete")
    public ResponseResultDto<Object> deleteBookInfoById(@PathVariable Long bookInfoId) {
        bookInfoService.deleteBookInfoById(bookInfoId);

        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 정보 삭제에 성공하였습니다")
                .data(null)
                .build();
    }
}
