package com.hae.library.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.dto.Book.Request.RequestBookApiDto;
import com.hae.library.dto.BookInfo.Request.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.Request.RequestIsbnDto;
import com.hae.library.dto.BookInfo.Response.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.Response.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.CommonErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.CategoryRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 책 정보 서비스입니다
@Slf4j
@Service
public class BookInfoService {

    private final BookInfoRepository bookInfoRepo;
    private final CategoryRepository categoryRepo;

    // @Value로 주입받아서 @RequiredArgsConstructor 에러가 나는 현상 때문에 생성자를 만들어서 주입받았습니다.
    BookInfoService(BookInfoRepository bookInfoRepo, CategoryRepository categoryRepo) {
        this.bookInfoRepo = bookInfoRepo;
        this.categoryRepo = categoryRepo;
    }

    @Value("${nationalIsbnApiKey}")
    private String nationalIsbnApiKey;

    /**
     * 새로운 책 정보를 저장합니다.
     *
     * @param requestBookDto 새로운 책 정보 요청 DTO
     * @return 저장된 책 정보 응답 DTO
     *
     * @throws BookErrorCode 책 정보 중복
     * @throws BookErrorCode 카테고리를 찾을 수 없음
     */
    @Transactional
    public BookInfo createBookInfo(RequestBookInfoDto requestBookDto) {
        // 중복되는 책 정보가 있는지 확인합니다.
        if (bookInfoRepo.existsByIsbn(requestBookDto.getIsbn()) == true) {
            throw new RestApiException(BookErrorCode.DUPLICATE_ISBN);
        }

        // 새로운 책 정보 객체를 생성합니다.
        BookInfo bookInfo = BookInfo.builder()
                .title(requestBookDto.getTitle())
                .author(requestBookDto.getAuthor())
                .isbn(requestBookDto.getIsbn())
                .image(requestBookDto.getImage())
                .publisher(requestBookDto.getPublisher())
                .publishedAt(requestBookDto.getPublishedAt())
                .build();

        // 카테고리를 조회하고 책 객체에 추가합니다.
        Optional<Category> categoryOptional =
                categoryRepo.findByCategoryName(requestBookDto.getCategoryName());
        if (!categoryOptional.isPresent()) {
            throw new RestApiException(BookErrorCode.CATEGORY_NOT_FOUND);
        }
        Category category = categoryOptional.get();
        // 책 정보에 카테고리를 추가합니다.
        bookInfo.addCategory(category);
        // 생성한 BookInfo 객체를 DB에 저장하고, 저장된 객체를 다시 가져옵니다.
        return bookInfoRepo.save(bookInfo);
    }

    /**
     * 검색어에 따라 모든 책 정보를 페이징하여 가져옵니다.
     *
     * @param search 검색어
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @param categoryName 카테고리 이름
     * @param sort   정렬 방식
     * @return 책 정보 페이지 응답 DTO
     */

    @Transactional
    public Page<ResponseBookInfoDto> getAllBookInfo(String search, int page, int size,
                                                    String categoryName, String sort) {
        // PageRequest 객체를 생성하여 페이지 번호, 페이지 크기, 정렬 방식을 설정합니다.
        Sort.Direction direction = sort.equals("최신도서") ?  Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        // Specification을 이용해 동적 쿼리를 생성합니다.
        Specification<BookInfo> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // bookList 필드가 비어있지 않은 BookInfo 객체를 검색합니다.
            predicates.add(cb.isNotEmpty(root.get("bookList")));

            if (search != null && !search.trim().isEmpty()) {
                // title 필드 또는 isbn 필드가 검색어를 포함하고 있는 BookInfo 객체를 검색합니다.
                Predicate titleLike = cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%");
                Predicate isbnLike = cb.like(cb.lower(root.get("isbn")), "%" + search.toLowerCase() + "%");
                predicates.add(cb.or(titleLike, isbnLike));
            }

            if (categoryName != null && !categoryName.trim().isEmpty() && !categoryName.equals("전체")) {
                // category 필드가 주어진 카테고리와 일치하는 BookInfo 객체를 검색합니다.
                predicates.add(cb.equal(root.get("category").get("categoryName"), categoryName));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 책 정보를 페이징하여 가져온 후, 가져온 책 정보를 Response DTO로 변환합니다.
        Page<BookInfo> bookInfoList = bookInfoRepo.findAll(spec, pageable);
        Page<ResponseBookInfoDto> responseBookInfoDtoList = bookInfoList.map(ResponseBookInfoDto::from);
        return responseBookInfoDtoList;
    }

    /**
     * ID로 책 정보를 가져옵니다.
     *
     * @param bookInfoId 책 정보 ID
     * @return 책 정보와 관련된 책 응답 DTO
     */
    @Transactional
    public ResponseBookInfoWithBookDto getBookInfoById(Long bookInfoId) {
        // bookInfo가 존재하는지 않으면 예외를 발생시킵니다.
        BookInfo bookInfo =
                bookInfoRepo.findById(bookInfoId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO));

        // 책 정보에 속한 실물 책들을 가져옵니다.
        return ResponseBookInfoWithBookDto.from(bookInfo);
    }

    /**
     * ISBN으로 책 정보를 가져옵니다.
     * 국립중앙도서관 API를 이용하여 책 정보를 가져옵니다.
     *
     * @param requestIsbnDto ISBN 정보 요청 DTO
     * @return 책 정보와 관련된 책 응답 DTO
     */
    @Transactional
    public ResponseBookInfoWithBookDto getBookInfoByIsbn(RequestIsbnDto requestIsbnDto) {
        String isbn = requestIsbnDto.getIsbn();
        // isbn이 null이거나 공백이라면 예외를 발생시킵니다.
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new RestApiException(BookErrorCode.NOTHING_REQUEST_INPUT);
        }

        // DB에서 isbn으로 책 정보를 가져옵니다.
        Optional<BookInfo> bookInfoOptional = bookInfoRepo.findByIsbn(isbn);
        BookInfo bookInfo;
        // 만약 책 정보가 없다면
        if (bookInfoOptional.isEmpty()) {
            // 국립중앙도서관 API로 책 정보를 가져옵니다.
            RequestBookApiDto requestBookApiDto = searchByIsbn(isbn);
            // api 못받으면 에러처리(result가 null이면)
            if (requestBookApiDto.getResult() == null) {
                throw new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO);
            }
            // 가져온 책 정보를 bookInfo에 저장합니다.
            return requestBookApiDto.toResponseBookInfoWithBookDto();
        // 책 정보가 있다면 그대로 가져옵니다.
        } else {
            bookInfo = bookInfoOptional.get();
        }
        return ResponseBookInfoWithBookDto.from(bookInfo);
    }

    /**
     * ISBN으로 국립중앙도서관 API에서 책 정보를 검색합니다.
     *
     * @param isbn ISBN
     * @return 도서 검색 결과 DTO
     */
    public RequestBookApiDto searchByIsbn(String isbn) {
        log.info("BookService - isbn : {}",isbn);

        // 헤더 설정을 합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        // 국립 중앙 도서관api 이용 url입니다.
        String seojiUrl =
                "https://www.nl.go.kr/NL/search/openApi/search.do?key=" + nationalIsbnApiKey +
                        "&detailSearch=true&apiType=json&isbnOp=isbn&isbnCode=" + isbn;
        RestTemplate restTemplate = new RestTemplate();

        //국립도서관 api 호출합니다.
        ResponseEntity<String> response = restTemplate.exchange(seojiUrl, HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        String jsonString = response.getBody();  // ResponseEntity에서 JSON 문자열 가져오기
        ObjectMapper mapper = new ObjectMapper();  // ObjectMapper 객체 생성

        // JSON 문자열을 RequestBookApiDto 객체로 변환합니다.
        RequestBookApiDto requestBookApiDto = null;
        try {
            requestBookApiDto = mapper.readValue(jsonString, RequestBookApiDto.class);
        } catch (JsonProcessingException e) {
            throw new RestApiException(CommonErrorCode.JSON_PROCESSING_EXCEPTION);
        }

        return requestBookApiDto;
    }

    /**
     * ID로 책 정보를 삭제합니다.
     *
     * @param id 책 정보 ID
     */
    @Transactional
    public void deleteBookInfoById(Long id) {
        // bookInfo가 존재하는지 않으면 예외를 발생시킵니다.
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        bookInfoRepo.delete(bookInfo);
    }
}
