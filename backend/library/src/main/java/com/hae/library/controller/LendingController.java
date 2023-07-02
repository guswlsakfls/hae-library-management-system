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
    @PostMapping(value = "/lending/create")
    public ResponseResultDto<Object> LendingBook(@RequestBody @Valid RequestLendingDto requestLendingDto) {
        ResponseLendingDto responseLendingDto = lendingService.lendingBook(requestLendingDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여에 성공하였습니다")
                .data(responseLendingDto)
                .build();
    }

    @PutMapping(value = "/lending/returning")
    public ResponseResultDto<Object> ReturningBook(@RequestBody @Valid RequestReturningDto requestReturningDto) {
        ResponseLendingDto responseLendingDto = lendingService.returningBook(requestReturningDto);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 반납에 성공하였습니다")
                .data(responseLendingDto)
                .build();
    }

    @GetMapping(value = "/lending/all")
    public ResponseResultDto<Object> getAllLendingHistory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size
    ) {
        log.error("search: {}, page: {}, size: {}", search, page, size);
        Page<ResponseLendingDto> responseLendingDtoList =
                lendingService.getAllLendingHistory(search, page, size);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("lendingList", responseLendingDtoList.getContent());
        responseData.put("totalElements", responseLendingDtoList.getTotalElements());
        responseData.put("currentPage", responseLendingDtoList.getNumber());
        responseData.put("size", responseLendingDtoList.getSize());
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기록 조회에 성공하였습니다")
                .data(responseData)
                .build();
    }

    @GetMapping(value = "/lending/me")
    public ResponseResultDto<Object> getMemberLendingHistory() {
        List<ResponseMemberLendingDto> responseMemberLendingDtoList =
                lendingService.getMemberLendingHistory();
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기록 조회에 성공하였습니다")
                .data(responseMemberLendingDtoList)
                .build();
    }


    @PutMapping(value = "/lending/{lendingId}/renew")
    public ResponseResultDto<Object> updateRenew(@PathVariable Long lendingId) {
        ResponseLendingDto responseLendingDto = lendingService.updateRenew(lendingId);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기간 연장에 성공하였습니다")
                .data(responseLendingDto)
                .build();
    }

    @DeleteMapping(value = "/lending/{lendingId}/delete")
    public ResponseResultDto<Object> deleteLending(@PathVariable Long lendingId) {
        ResponseMemberLendingDto responsMemberLendingDto = lendingService.deleteLending(lendingId);
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기록 삭제에 성공하였습니다")
                .data(responsMemberLendingDto)
                .build();
    }
}
