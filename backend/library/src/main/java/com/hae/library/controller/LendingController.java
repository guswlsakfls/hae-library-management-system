package com.hae.library.controller;

import com.hae.library.dto.Lending.RequestLendingDto;
import com.hae.library.dto.Lending.RequestReturningDto;
import com.hae.library.dto.Lending.ResponseLendingDto;
import com.hae.library.dto.Lending.ResponseMemberLendingDto;
import com.hae.library.dto.Member.ResponseMemberDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.service.LendingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class LendingController {
    private final LendingService lendingService;

    // 책 대여 요청을 합니다.
    @PostMapping(value = "/lending/create")
    public ResponseResultDto<Object> LendingBook(@RequestBody @Valid RequestLendingDto requestLendingDto) {
        log.info("책 대여 요청: [POST] /lending/create - {}", requestLendingDto.toString());
        ResponseLendingDto responseLendingDto = lendingService.lendingBook(requestLendingDto);

        log.info("책 대여에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여에 성공하였습니다")
                .data(responseLendingDto)
                .build();
    }

    // 책 반납 요청을 합니다.
    @PutMapping(value = "/lending/returning")
    public ResponseResultDto<Object> ReturningBook(@RequestBody @Valid RequestReturningDto requestReturningDto) {
        log.info("책 반납 요청: [PUT] /lending/returning - {}", requestReturningDto.toString());
        ResponseLendingDto responseLendingDto = lendingService.returningBook(requestReturningDto);

        log.info("책 반납에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 반납에 성공하였습니다")
                .data(responseLendingDto)
                .build();
    }

    // 책 대여 기록을 조회합니다.
    @GetMapping(value = "/lending/all")
    public ResponseResultDto<Object> getAllLendingHistory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ) {
        log.info("책 대여 기록 조회: [GET] /lending/all - search: {}, page: {}, size: {}", search, page,
                size);
        Page<ResponseLendingDto> responseLendingDtoList =
                lendingService.getAllLendingHistory(search, page, size);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("lendingList", responseLendingDtoList.getContent());
        responseData.put("totalElements", responseLendingDtoList.getTotalElements());
        responseData.put("currentPage", responseLendingDtoList.getNumber());
        responseData.put("size", responseLendingDtoList.getSize());

        log.info("책 대여 기록 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기록 조회에 성공하였습니다")
                .data(responseData)
                .build();
    }

    // 책 대여 기록을 조회합니다.
    @GetMapping(value = "/lending/me")
    public ResponseResultDto<Object> getMemberLendingHistory() {
        log.info("책 대여 기록 조회: [GET] /lending/me");
        List<ResponseMemberLendingDto> responseMemberLendingDtoList =
                lendingService.getMemberLendingHistory();

        log.info("책 대여 기록 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기록 조회에 성공하였습니다")
                .data(responseMemberLendingDtoList)
                .build();
    }

    // 책 대여 기간 연장을 합니다.
    @PutMapping(value = "/lending/{lendingId}/renew")
    public ResponseResultDto<Object> updateRenew(@PathVariable Long lendingId) {
        log.info("책 대여 기간 연장: [PUT] /lending/{lendingId}/renew - lendingId: {}", lendingId);
        ResponseLendingDto responseLendingDto = lendingService.updateRenew(lendingId);

        log.info("책 대여 기간 연장에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기간 연장에 성공하였습니다")
                .data(responseLendingDto)
                .build();
    }

    // 책 대여 기록을 삭제합니다.
    @DeleteMapping(value = "/lending/{lendingId}/delete")
    public ResponseResultDto<Object> deleteLending(@PathVariable Long lendingId) {
        log.info("책 대여 기록 삭제: [DELETE] /lending/{lendingId}/delete - lendingId: {}", lendingId);
        ResponseMemberLendingDto responsMemberLendingDto = lendingService.deleteLending(lendingId);

        log.info("책 대여 기록 삭제에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기록 삭제에 성공하였습니다")
                .data(responsMemberLendingDto)
                .build();
    }
}
