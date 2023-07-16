package com.hae.library.controller;

import com.hae.library.dto.Common.ResponseResultDto;
import com.hae.library.dto.Lending.Request.RequestCallsignDto;
import com.hae.library.dto.Lending.Request.RequestLendingDto;
import com.hae.library.dto.Lending.Request.RequestReturningDto;
import com.hae.library.dto.Lending.Response.ResponseLendingDto;
import com.hae.library.dto.Lending.Response.ResponseLendingInfoForReturningDto;
import com.hae.library.dto.Lending.Response.ResponseMemberLendingDto;
import com.hae.library.service.LendingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
public class LendingController {
    private final LendingService lendingService;

    // 책 대여 요청 정보를 받아 저장합니다.
    @PostMapping(value = "/admin/lending/create")
    public ResponseResultDto<Object> LendingBook(@RequestBody @Valid RequestLendingDto requestLendingDto) {
        log.info("책 대여 요청: [POST] /lending/create - {}", requestLendingDto.toString());
        lendingService.lendingBook(requestLendingDto);

        log.info("책 대여에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여에 성공하였습니다")
                .build();
    }

    // 책 반납 요청을 받아 처리합니다.
    @PutMapping(value = "/admin/lending/returning")
    public ResponseResultDto<Object> ReturningBook(@RequestBody @Valid RequestReturningDto requestReturningDto) {
        log.info("책 반납 요청: [PUT] /lending/returning - {}", requestReturningDto.toString());
        lendingService.returningBook(requestReturningDto);

        log.info("책 반납에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 반납에 성공하였습니다")
                .build();
    }

    // 반납을 위한 책 대여 기록을 조회합니다.
    @GetMapping(value = "/admin/lending/callsign")
    public ResponseResultDto<Object> getLendingInfoByCallSign(@Valid RequestCallsignDto requestCallsignDto) {
        log.info("반납을 위한 책 대여 기록 조회: [GET] /lending/callsign - callSign: {}", requestCallsignDto.toString());
        ResponseLendingInfoForReturningDto responseLendingDto =
                lendingService.getLendingInfoByCallSign(requestCallsignDto);

        log.info("반납을 위한 책 대여 기록 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("반납을 위한 책 대여 기록 조회에 성공하였습니다")
                .data(responseLendingDto)
                .build();
    }

    // 모든 책 대여 기록을 조회합니다.
    @GetMapping(value = "/admin/lending/all")
    public ResponseResultDto<Object> getAllLendingHistory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) String isLendingOrReturning,
            @RequestParam(required = false) String sort
    ) {
        log.info("책 대여 기록 조회: [GET] /lending/all - search: {}, page: {}, size: {}, :isRenderingOrReturning: {}, sort: {}",
                search, page,
                size, isLendingOrReturning, sort);
        Page<ResponseLendingDto> responseLendingDtoList =
                lendingService.getAllLendingHistory(search, page, size, isLendingOrReturning, sort);

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

    // 자신의 책 대여 기록을 조회합니다.
    @GetMapping(value = "/member/lending-history/me")
    public ResponseResultDto<Object> getMemberLendingHistory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) String isLendingOrReturning,
            @RequestParam(required = false) String sort
    ) {
        log.info("책 대여 기록 조회: [GET] /lending/me - search: {}, page: {}, size: {}, :isRenderingOrReturning: {}, sort: {}",
                search, page, size, isLendingOrReturning, sort);
        Page<ResponseMemberLendingDto> responseMemberLendingDtoList =
                lendingService.getMemberLendingHistory(search, page, size, isLendingOrReturning, sort);

        // 회원이 빌린 대출/반납 기록 리스트 와 페이지 네이션 정보를 데이터로 설정합니다.
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("myLendingHistory", responseMemberLendingDtoList.getContent());
        responseData.put("totalElements", responseMemberLendingDtoList.getTotalElements());
        responseData.put("currentPage", responseMemberLendingDtoList.getNumber());
        responseData.put("size", responseMemberLendingDtoList.getSize());

        log.info("책 대여 기록 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기록 조회에 성공하였습니다")
                .data(responseData)
                .build();
    }

    // 책 대여 기록을 삭제합니다.
    @DeleteMapping(value = "/admin/lending/{lendingId}/delete")
    public ResponseResultDto<Object> deleteLending(@PathVariable Long lendingId) {
        log.info("책 대여 기록 삭제: [DELETE] /lending/{lendingId}/delete - lendingId: {}", lendingId);
        lendingService.deleteLending(lendingId);

        log.info("책 대여 기록 삭제에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("책 대여 기록 삭제에 성공하였습니다")
                .build();
    }
}
