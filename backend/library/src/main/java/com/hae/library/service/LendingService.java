package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import com.hae.library.dto.Lending.RequestLendingDto;
import com.hae.library.dto.Lending.RequestReturningDto;
import com.hae.library.dto.Lending.ResponseLendingDto;
import com.hae.library.dto.Lending.ResponseMemberLendingDto;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.LendingRepository;
import com.hae.library.repository.MemberRepository;
import com.hae.library.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@AllArgsConstructor
@Service
public class LendingService {
    private final LendingRepository lendingRepo;
    private final BookRepository bookRepo;
    private final MemberRepository memberRepo;

    @Transactional
    public ResponseLendingDto lendingBook(RequestLendingDto requestLendingDto) {
        // 도서가 있는지 조회
        Book book =
                bookRepo.findById(requestLendingDto.getBookId()).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO_BY_ID));

        // 대출된 도서인지 확인
        Boolean existslending =
                lendingRepo.existsByBookId(book.getId());
        if (existslending) {
            throw new RestApiException(BookErrorCode.BOOK_ALREADY_LENT);
        }

        // 대출받는 유저가 회원인지 확인
        Member user =
                memberRepo.findById(requestLendingDto.getUserId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 대출자 회원인지 확인
        Member lendingLibrarian = memberRepo.findById(requestLendingDto.getLendingLibrarianId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        log.error("{}, {}, {} {}", requestLendingDto.getLendingCondition(),
                requestLendingDto.getBookId(),
                requestLendingDto.getUserId(),
                requestLendingDto.getLendingLibrarianId());

        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).plusWeeks(2);

        Lending newLending = Lending.builder()
                .book(book)
                .user(user)
                .lendingLibrarian(lendingLibrarian)
                .lendingCondition(requestLendingDto.getLendingCondition())
                .returningEndAt(now)
                .build();

        lendingRepo.save(newLending);


        return ResponseLendingDto.from(newLending);
    }

    @Transactional
    public ResponseLendingDto returningBook(RequestReturningDto requestReturningDto) {
        // 도서대출 중인지 조회 없으면 예외처리
        Lending lending =
                lendingRepo.findById(requestReturningDto.getLendingId()).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING_BY_ID));

        // 대출된 도서인지 확인
        if (lending.getReturningLibrarian() != null) {
            throw new RestApiException(BookErrorCode.BOOK_ALREADY_RETURNED);
        }
        // TODO: 대출된 도서가 저장 되어 있으니까 도서, 대출사서, 대출 컨디션은 있다고 생각을 하는것이 맞을까?

        // 도서반납자 회원인지 확인
        Member user =
                memberRepo.findById(lending.getUser().getId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 반납사서가 회원인지 확인
        Member returningLibrarian = memberRepo.findById(requestReturningDto.getReturningLibrarianId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 반납일이 지났는지 확인
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        if (lending.getReturningEndAt().isAfter(now)) {
            // 지났으면 user 테이블의 penalty를 날짜 지난만큼 더해준다.
            long daysOverdue = ChronoUnit.DAYS.between(lending.getReturningEndAt(), now);
            LocalDateTime newPenaltyEndDate = user.getPenaltyEndDate();
            if (newPenaltyEndDate == null) {
                newPenaltyEndDate = now.plusDays(daysOverdue);
            } else { // 연체된 책을 반납하고 다른 책을 또 연체하면 penaltyEndDate가 누적되어야 한다.
                newPenaltyEndDate = newPenaltyEndDate.plusDays(daysOverdue);
            }
            user.updatePenaltyEndDate(newPenaltyEndDate);
            memberRepo.save(user);
        }

        // 반납 로직 구현
        lending.updateReturning(returningLibrarian, requestReturningDto.getReturningCondition(),
                now);

        Lending updateLending = lendingRepo.save(lending);

        return ResponseLendingDto.from(updateLending);
    }
    @Transactional
    public List<ResponseLendingDto> getAllLendingHistory() {
        // 전체 대출 기록 조회 로직 구현
        List<Lending> lendingList = lendingRepo.findAll();
        List<ResponseLendingDto> responseLendingDtoList = new ArrayList<>();
        for (Lending lending : lendingList) {
            responseLendingDtoList.add(ResponseLendingDto.from(lending));
        }

        return responseLendingDtoList;
    }

    @Transactional
    public List<ResponseMemberLendingDto> getMemberLendingHistory() {
        // 개별 대출 기록 조회 없으면 예외처리
        Member user =
                memberRepo.findById(SecurityUtil.getCurrentMemberId()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<Lending> lendingList = lendingRepo.findAllByUser(user);
        List<ResponseMemberLendingDto> responseMemberLendingDtoList = new ArrayList<>();
        for (Lending lending : lendingList) {
            responseMemberLendingDtoList.add(ResponseMemberLendingDto.from(lending));
        }

        return responseMemberLendingDtoList;
    }


    // 유저가 대출 연장 버튼 클릭(예약이 있으면 불가)
    @Transactional
    public ResponseLendingDto updateRenew(Long lendingId) {
        // 개별 대출 기록 조회 없으면 예외처리
        Lending lending =
                lendingRepo.findById(lendingId).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING_BY_ID));

        // 예약이 있는지 확인
        if (lending.isRenew() == true) {
            throw new RestApiException(BookErrorCode.BOOK_ALREADY_RENEWED);
        }
        lending.renewLending();

        Lending updateLending = lendingRepo.save(lending);

        return ResponseLendingDto.from(updateLending);
    }

    @Transactional
    public ResponseMemberLendingDto deleteLending(Long lendingId) {
        // 개별 대출 기록 조회 없으면 예외처리
        Lending lending =
                lendingRepo.findById(lendingId).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING_BY_ID));

        // TODO: 예약이 있는지 확인(예약 엔티티 관련)

        // TODO: 삭제 ?삭제하면 무조건 void 반환?
        lendingRepo.delete(lending);

        return null;
    }
}
