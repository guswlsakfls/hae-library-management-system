package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import com.hae.library.dto.Lending.*;
import com.hae.library.dto.ResponseResultDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.LendingRepository;
import com.hae.library.repository.MemberRepository;
import com.hae.library.util.SecurityUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    /**
     * 책 대출을 처리합니다.
     *
     * @param requestLendingDto 대출 요청 DTO
     * @return 대출 정보 DTO
     */
    @Transactional
    public ResponseLendingDto lendingBook(RequestLendingDto requestLendingDto) {
        // 도서가 있는지 조회합니다.
        Book book = bookRepo.findById(requestLendingDto.getBookId())
                .orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO));

        // 대출된 도서인지 확인합니다.
        if (book.getLending() != null) {
            throw new RestApiException(BookErrorCode.BOOK_ALREADY_LENT);
        }

        // 대출받는 유저가 회원인지 확인하고 가져옵니다.
        Member user = memberRepo.findById(requestLendingDto.getUserId())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.USER_NOT_FOUND));

        // 대출자 회원인지 확인하고 가져옵니다.
        Member lendingLibrarian = memberRepo.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.ADMIN_NOT_FOUND));

        // 대출일을 2주뒤 00:00:00으로 설정합니다.
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).plusWeeks(2);

        Lending newLending = Lending.builder()
                .user(user)
                .lendingLibrarian(lendingLibrarian)
                .lendingCondition(requestLendingDto.getLendingCondition())
                .returningEndAt(now)
                .build();

        // 대출 정보를 저장합니다.
        newLending.addBook(book);
        lendingRepo.save(newLending);

        return ResponseLendingDto.from(newLending);
    }

    /**
     * 책 반납을 처리합니다.
     *
     * @param requestReturningDto 반납 요청 DTO
     * @return 반납된 대출 정보 DTO
     */
    @Transactional
    public ResponseLendingDto returningBook(RequestReturningDto requestReturningDto) {
        // 대출 도서인지 확인합니다.
        Lending lending = lendingRepo.findById(requestReturningDto.getLendingId())
                .orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING));

        // 반납된 도서인지 확인합니다.
        if (lending.getReturningLibrarian() != null) {
            throw new RestApiException(BookErrorCode.BOOK_ALREADY_RETURNED);
        }

        // 도서 반납자가 회원인지 확인하고 회원 정보를 가져옵니다.
        Member user = memberRepo.findById(lending.getUser().getId())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 당일 00:00:00을 가져옵니다.
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        // 반납일이 지났는지 확인하고 연체일을 부과합니다.
        if (lending.getReturningEndAt().isAfter(now)) {
            // 연체일을 계산합니다.
            long daysOverdue = ChronoUnit.DAYS.between(lending.getReturningEndAt(), now);
            LocalDateTime newPenaltyEndDate = user.getPenaltyEndDate();
            // 연체일이 0일 이상이면 연체일을 부과합니다.
            if (newPenaltyEndDate == null) { // 연체일이 없으면 연체일을 부과합니다.
                newPenaltyEndDate = now.plusDays(daysOverdue);
            } else { // 연체일이 있으면 연체일을 더합니다.
                newPenaltyEndDate = newPenaltyEndDate.plusDays(daysOverdue);
            }
            // 연체일을 저장합니다.
            user.updatePenaltyEndDate(newPenaltyEndDate);
            memberRepo.save(user);
        }

        // 반납자 회원인지 확인하고 회원 정보를 가져옵니다.
        Member returningLibrarian = memberRepo.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.ADMIN_NOT_FOUND));

        // 반납 처리정보를 업데이트 합니다.
        lending.updateReturning(returningLibrarian, requestReturningDto.getReturningCondition(), now);
        Lending updatedLending = lendingRepo.save(lending);

        return ResponseLendingDto.from(updatedLending);
    }

    /**
     * 책 반납을 위한 대출 정보를 조회합니다.
     *
     * @param callsign 책 청구기호
     * @return 대출 정보 DTO
     */
    @Transactional
    public ResponseLendingInfoForReturningDto getLendingInfoByCallSign(String callsign) {
        // 책 청구기호로 책을 조회합니다.
        Book book = bookRepo.findByCallSign(callsign)
                .orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));

        // 책의 대출 정보를 조회합니다.
        Lending lending = lendingRepo.findByBookId(book.getId())
                .orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING));

        return ResponseLendingInfoForReturningDto.from(lending);
    }

    /**
     * 모든 대출 기록을 페이징하여 조회합니다.
     *
     * @param search 검색어
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @return 대출 및 페이지 정보
     */
    @Transactional
    public Page<ResponseLendingDto> getAllLendingHistory(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        // Specification을 이용해 동적 쿼리 생성
        Specification<Lending> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction(); // 모든 결과 반환
            }
            // 검색어(도서 제목, 이메일)가 포함된 경우 해당 결과 반환합니다.
            return cb.or(cb.like(cb.lower(root.get("book").get("bookInfo").get("title")),
                            "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("user").get("email")), "%" + search.toLowerCase() +
                            "%")
            );
        };

        // 전체 대출 기록 조회
        Page<Lending> lendingList = lendingRepo.findAll(spec, pageable);
        Page<ResponseLendingDto> responseLendingDtoList = lendingList.map(ResponseLendingDto::from);

        return responseLendingDtoList;
    }


    /**
     * 특정 회원의 대출 기록을 조회합니다.
     *
     * @return 회원의 대출 기록 리스트
     */
    @Transactional
    public Page<ResponseMemberLendingDto> getMemberLendingHistory(String search, int page, int size) {
        // 페이징 정보를 설정합니다.
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        // 로그인한 회원을 조회합니다. (jwt 토큰에서 회원 ID를 가져옵니다.)
        Member user = memberRepo.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // Specification을 이용해 동적 쿼리를 생성합니다.
        Specification<Lending> spec = (root, query, cb) -> {
            Predicate p = cb.equal(root.get("user"), user);

            if (search != null && !search.trim().isEmpty()) {
                // 검색어가 포함된 경우 해당 결과를 추가합니다.
                p = cb.and(p, cb.like(cb.lower(root.get("book").get("bookInfo").get("title")),
                        "%" + search.toLowerCase() + "%"));
            }

            return p;
        };

        // 로그인한 회원의 대출 기록을 조회합니다.
        Page<Lending> lendingList = lendingRepo.findAll(spec, pageable);

        // 대출 기록들을 DTO로 변환합니다.
        Page<ResponseMemberLendingDto> responseMemberLendingDtoList = lendingList.map(ResponseMemberLendingDto::from);

        return responseMemberLendingDtoList;
    }


    /**
     * 대출 연장을 처리합니다.
     *
     * @param lendingId 대출 ID
     * @return 연장된 대출 정보 DTO
     */
    @Transactional
    public ResponseLendingDto updateRenew(Long lendingId) {
        // 대출Id로 대출 정보를 조회합니다.
        Lending lending = lendingRepo.findById(lendingId)
                .orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING));

        // 대출 연장이 가능한지 확인합니다.
        if (lending.isRenew()) {
            throw new RestApiException(BookErrorCode.BOOK_ALREADY_RENEWED);
        }

        // 대출 연장을 처리합니다.
        lending.renewLending();
        Lending updatedLending = lendingRepo.save(lending);

        return ResponseLendingDto.from(updatedLending);
    }

    /**
     * 대출 기록을 삭제합니다.
     *
     * @param lendingId 대출 ID
     * @return 삭제된 대출 정보 DTO
     */
    @Transactional
    public ResponseMemberLendingDto deleteLending(Long lendingId) {
        // 대출Id로 대출 정보를 조회합니다.
        Lending lending = lendingRepo.findById(lendingId)
                .orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING));

        // 대출 기록을 삭제합니다.
        lendingRepo.delete(lending);

        return null;
    }
}
