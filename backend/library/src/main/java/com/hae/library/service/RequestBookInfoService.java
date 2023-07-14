package com.hae.library.service;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Member;
import com.hae.library.domain.RequestBook;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.repository.MemberRepository;
import com.hae.library.repository.RequestBookInfoRepository;
import com.hae.library.util.SecurityUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestBookInfoService {
    private final RequestBookInfoRepository requestBookInfoRepo;
    private final BookInfoService bookInfoService;
    private final MemberRepository memberRepository;

    /**
     * 새로운 책 요청 정보를 저장합니다.
     * @param requestCreateCategoryDto
     */
    public void createRequestBookInfo(RequestBookInfoDto requestCreateCategoryDto) {
        // 현재 보안 컨텍스트에서 인증된 사용자의 이메일로 회원 정보를 찾습니다. 없다면, 예외를 발생시킵니다.
        Member member =
                memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(() -> new RestApiException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 새로운 책 정보를 생성하고 중복되는 isbn이 존재할 시 예외처리 됩니다.
        BookInfo newBookInfo = bookInfoService.createBookInfo(requestCreateCategoryDto);

        // 새로운 책 정보를 가지고 새로운 책 요청 정보를 생성합니다.
        RequestBook newRequestBook = RequestBook.builder()
                .bookInfo(newBookInfo)
                .member(member)
                .isApproved(false)
                .build();
        newRequestBook.addMapping(member ,newBookInfo);

        // 새로운 책 요청 정보를 저장합니다.
        requestBookInfoRepo.save(newRequestBook);
    }

    /**
     * 구매요청 도서 정보를 조회합니다.
     * @param search
     * @param page
     * @param size
     * @param category
     * @param sort
     * @return Page<ResponseBookInfoWithBookDto>
     */

    // 도서 구매 요청들을 조회합니다.
    public Page<ResponseBookInfoDto> getRequestBookInfoList(String search, int page, int size,
                                                            String categoryName, String sort,
                                                            String approved) {
        // PageRequest 객체를 생성하여 페이지 번호, 페이지 크기, 정렬 방식을 설정합니다.
        Sort.Direction direction = sort.equals("최신도서") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        // Specification을 이용해 동적 쿼리를 생성합니다.
        Specification<RequestBook> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.trim().isEmpty()) {
                // title 필드 또는 isbn 필드가 검색어를 포함하고 있는 BookInfo 객체를 검색합니다.
                Predicate titleLike = cb.like(cb.lower(root.get("bookInfo").get("title")),
                        "%" + search.toLowerCase() + "%");
                Predicate isbnLike = cb.like(cb.lower(root.get("bookInfo").get("isbn")), "%" + search.toLowerCase() + "%");
                Predicate memberEmailLike = cb.like(cb.lower(root.get("member").get("email")), "%" + search.toLowerCase() + "%");
                predicates.add(cb.or(titleLike, isbnLike, memberEmailLike));
            }

            if (categoryName != null && !categoryName.trim().isEmpty() && !categoryName.equals("전체")) {
                // category 필드가 주어진 카테고리와 일치하는 BookInfo 객체를 검색합니다.
                predicates.add(cb.equal(root.get("bookInfo").get("category").get("categoryName"),
                        categoryName));
            }

            if (approved != null && !approved.trim().isEmpty() && !approved.equals("전체")) {
                if (approved.equals("구매완료")) {
                    predicates.add(cb.isTrue(root.get("isApproved")));
                } else if (approved.equals("미승인")) {
                    predicates.add(cb.isFalse(root.get("isApproved")));
                }
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 구매요청 도서 정보를 페이징하여 가져온 후, 가져온 책 정보를 Response DTO로 변환합니다.
        Page<RequestBook> requestBookList = requestBookInfoRepo.findAll(spec, pageable);
        Page<ResponseBookInfoDto> responseBookInfoList =
                requestBookList.map(ResponseBookInfoDto::from);
        return responseBookInfoList;
    }

    /**
     * 구매요청 도서를 삭제합니다.
     * @param requestBookId
     *
     * @throws BookErrorCode 존재하지 않는 구매 요청 도서 정보입니다.
     */
    public void deleteRequestBookInfo(Long requestBookId) {
        // 요청 도서 정보를 조회합니다.
        RequestBook requestBook =
                requestBookInfoRepo.findById(requestBookId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO));

        // 요청 도서 정보를 삭제합니다.
        requestBookInfoRepo.delete(requestBook);
    }
}
