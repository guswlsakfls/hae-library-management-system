package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Enum.BookStatus;
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
        // 도서가 대출 중인지 확인합니다.
        if (book.isLendingStatus()) {
            throw new RestApiException(BookErrorCode.BOOK_ALREADY_LENT);
        }
        // 분실도서인지 확인합니다.
        if (book.getStatus() == BookStatus.LOST) {
            throw new RestApiException(BookErrorCode.BOOK_LOST);
        }
        // 도서를 대출 처리합니다.
        book.updateLending();
        bookRepo.save(book);

        // 대출받는 유저가 회원인지 확인하고 가져옵니다.
        Member user = memberRepo.findById(requestLendingDto.getUserId())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.USER_NOT_FOUND));
        // 대출 받는 유저의 대출 권수를 확인하고, 대출 가능 여부를 확인합니다.
        if (user.getLendingCount() > 3) {
            throw new RestApiException(MemberErrorCode.USER_NOT_LENDING_AVAILABLE);
        }
        // 대출 받는 유저의 연체현황을 확인하고, 대출 가능 여부를 확인합니다.
        if (user.isPenalty()) {
            throw new RestApiException(MemberErrorCode.USER_OVERDUE);
        }
        // 연체일이 지난상태에서 대출받으면 연체일을 초기화 해줍니다.
        if (user.getPenaltyEndDate() != null) {
            user.resetPenaltyEndDate();
        }
        // 대출 받는 유저의 대출 횟수를 증가시킵니다.
        user.increaseLendingCount();
        memberRepo.save(user);

        // 대출자 회원인지 확인하고 가져옵니다.
        Member lendingLibrarian = memberRepo.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.ADMIN_NOT_FOUND));

        // 대출일을 2주뒤 00:00:00으로 설정합니다.
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).plusWeeks(2);

        Lending newLending = Lending.builder()
                .user(user)
                .lendingLibrarian(lendingLibrarian)
                .lendingCondition(requestLendingDto.getLendingCondition())
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
     */
    @Transactional
    public void returningBook(RequestReturningDto requestReturningDto) {
        // 대출 도서인지 확인하고 대출 정보를 가져옵니다.
        Lending lending = lendingRepo.findById(requestReturningDto.getLendingId())
                .orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING));
        // 반납 도서를 가져옵니다.
        Book book = lending.getBook();
        // 반납자 회원인지 확인하고 회원 정보를 가져옵니다.
        Member returningLibrarian = memberRepo.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.ADMIN_NOT_FOUND));
        // 반납하는 유저 정보를 가져옵니다.
        Member user = lending.getUser();

        // 반납된 도서인지 확인합니다.
        if (book.isLendingStatus() == false) {
            throw new RestApiException(BookErrorCode.BOOK_ALREADY_RETURNED);
        }
        // 도서를 반납 처리합니다.
        book.updateReturning();
        bookRepo.save(book);

        // 유저의 대출 횟수를 감소시킵니다.
        user.decreaseLendingCount();

        // 반납일이 지났는지 확인하고 연체일을 부과합니다.
        // 당일 00:00:00을 가져옵니다.
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        // 대출일 2주 뒤에 날짜로 반납일을 정해줍니다.
        LocalDateTime returningEndAt = lending.getCreatedAt().plusDays(14);
        // 반납일이 지났는지 확인합니다.
        if (returningEndAt.isBefore(now)) {
            // 연체일을 계산합니다.
            long daysOverdue = ChronoUnit.DAYS.between(returningEndAt, now);
            LocalDateTime newPenaltyEndDate = user.getPenaltyEndDate();
            // 연체일이 없거나, 이미지난 연체일이 있으면 현재 시간부터 연체일을 부과합니다.
            if (newPenaltyEndDate == null || newPenaltyEndDate.isBefore(now)) {
                newPenaltyEndDate = now.plusDays(daysOverdue);
            } else { // 연체일이 있으면 현재 연체일에 부과합니다.
                newPenaltyEndDate = newPenaltyEndDate.plusDays(daysOverdue);
            }
            // 연체일을 부과하고 저장합니다.
            user.updatePenaltyEndDate(newPenaltyEndDate);
        }
        memberRepo.save(user);

        // 반납 처리정보를 업데이트 합니다.
        lending.updateReturning(returningLibrarian, requestReturningDto.getReturningCondition(), now);
        lendingRepo.save(lending);

//        return ResponseLendingDto.from(updatedLending);
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
        Lending lending = lendingRepo.findByBookIdAndReturningLibrarianIsNull(book.getId())
                .orElseThrow(() -> new RestApiException(BookErrorCode.NOT_LENDING));

        return ResponseLendingInfoForReturningDto.from(lending);
    }

    /**
     * 모든 대출 기록을 페이징하여 조회합니다.
     *
     * @param search 검색어
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @param isLendingOrReturning 대출/반납 여부
     * @param sort   정렬 방식
     * @return 대출 및 페이지 정보
     */
    @Transactional
    public Page<ResponseLendingDto> getAllLendingHistory(String search, int page, int size,
                                                         String isLendingOrReturning, String sort) {
        // 정렬 방식을 설정합니다.
        Sort.Direction direction = sort.equals("최신순") ?  Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        Specification<Lending> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 검색어가 있는 경우 해당 검색어를 포함하는 결과를 반환합니다.
            if (search != null && !search.trim().isEmpty()) {
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("book").get("bookInfo").get("title")), "%" + search.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("user").get("email")), "%" + search.toLowerCase() + "%"))
                );
            }

            // isLendingOrReturning 값에 따른 조건을 추가합니다.
            if (isLendingOrReturning != null && !isLendingOrReturning.trim().isEmpty()) {
                switch (isLendingOrReturning) {
                    case "대출 중":
                        // returningAt 값이 없는 경우를 찾음
                        predicates.add(cb.isNull(root.get("returningEndAt")));
                        break;
                    case "반납 완료":
                        // returningAt 값이 있는 경우를 찾음
                        predicates.add(cb.isNotNull(root.get("returningEndAt")));
                        break;
                    case "전체":
                        // 전체의 경우 추가 조건 없음
                        break;
                    default:
                        throw new RestApiException(BookErrorCode.BAD_REQUEST_BOOK);
                }
            }

            // 조합된 조건들로 쿼리 생성합니다.
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 위의 spec를 사용하여 대출 기록 조회합니다.
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
    public Page<ResponseMemberLendingDto> getMemberLendingHistory(String search, int page,
                                                                  int size, String isLendingOrReturning, String sort) {
        // 정렬 방식을 설정합니다.
        Sort.Direction direction = sort.equals("최신순") ?  Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        // 로그인한 회원을 조회합니다. (jwt 토큰에서 회원 ID를 가져옵니다.)
        Member user = memberRepo.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        Specification<Lending> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 검색어가 있는 경우 해당 검색어를 포함하는 결과를 반환합니다.
            if (search != null && !search.trim().isEmpty()) {
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("book").get("bookInfo").get("title")), "%" + search.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("book").get("bookInfo").get("isbn")),
                                "%" + search.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("book").get("callSign")),
                                "%" + search.toLowerCase() + "%"))
                );
            }

            // isLendingOrReturning 값에 따른 조건을 추가합니다.
            if (isLendingOrReturning != null && !isLendingOrReturning.trim().isEmpty()) {
                switch (isLendingOrReturning) {
                    case "대출 중":
                        // returningAt 값이 없는 경우를 찾음
                        predicates.add(cb.isNull(root.get("returningEndAt")));
                        break;
                    case "반납 완료":
                        // returningAt 값이 있는 경우를 찾음
                        predicates.add(cb.isNotNull(root.get("returningEndAt")));
                        break;
                    case "전체":
                        // 전체의 경우 추가 조건 없음
                        break;
                    default:
                        throw new RestApiException(BookErrorCode.BAD_REQUEST_BOOK);
                }
            }

            // 조합된 조건들로 쿼리 생성합니다.
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 로그인한 회원의 대출 기록을 조회합니다.
        Page<Lending> lendingList = lendingRepo.findAll(spec, pageable);

        // 대출 기록들을 DTO로 변환합니다.
        Page<ResponseMemberLendingDto> responseMemberLendingDtoList = lendingList.map(ResponseMemberLendingDto::from);

        return responseMemberLendingDtoList;
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
