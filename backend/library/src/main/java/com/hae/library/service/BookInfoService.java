package com.hae.library.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.dto.Book.RequestBookApiDto;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.CategoryRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookInfoService {

    private final BookInfoRepository bookInfoRepo;
    private final CategoryRepository categoryRepo;

    // TODO: @Value로 주입받아서 @RequiredArgsConstructor 에러가 나는 현상?
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
     */
    @Transactional
    public BookInfo createBookInfo(RequestBookWithBookInfoDto requestBookDto) {
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
     * @return 책 정보 페이지 응답 DTO
     */
//    @Transactional
//    public Page<ResponseBookInfoDto> getAllBookInfo(String search, int page, int size, String category, String sort) {
//        // PageRequest 객체를 생성하여 페이지 번호, 페이지 크기, 정렬 방식을 설정합니다.
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
//
//        // Specification을 이용해 동적 쿼리를 생성합니다.
//        Specification<BookInfo> spec = (root, query, cb) -> {
//            if (search == null || search.trim().isEmpty()) {
//                return cb.conjunction(); // 모든 결과를 반환합니다.
//            }
//            // title 필드가 검색어를 포함하고 있는 BookInfo 객체를 검색합니다.
//            return cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%");
//        };
//
//        // 책 정보를 페이징하여 가져온 후, 가져온 책 정보를 Response DTO로 변환합니다.
//        Page<BookInfo> bookInfoList = bookInfoRepo.findAll(spec, pageable);
//        Page<ResponseBookInfoDto> responseBookInfoDtoList = bookInfoList.map(ResponseBookInfoDto::from);
//        return responseBookInfoDtoList;
//    }

    @Transactional
    public Page<ResponseBookInfoDto> getAllBookInfo(String search, int page, int size,
                                                    String categoryName, String sort) {
        // PageRequest 객체를 생성하여 페이지 번호, 페이지 크기, 정렬 방식을 설정합니다.
        Sort.Direction direction = sort.equals("최신도서") ?  Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));

        // Specification을 이용해 동적 쿼리를 생성합니다.
        Specification<BookInfo> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

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
        // bookInfoId가 null이라면 예외를 발생시킵니다.
        BookInfo bookInfo =
                bookInfoRepo.findById(bookInfoId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO));
        // 책 정보에 속한 실물 책들을 가져옵니다.
        List<Book> bookList = bookInfo.getBookList();
        return ResponseBookInfoWithBookDto.from(bookInfo);
    }

    /**
     * ISBN으로 책 정보를 가져옵니다.
     *
     * @param isbn ISBN
     * @return 책 정보와 관련된 책 응답 DTO
     */
    @Transactional
    public ResponseBookInfoWithBookDto getBookInfoByIsbn(String isbn) {
        // isbn이 null이거나 공백이라면
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
            log.error("requestBookApiDto: {}", requestBookApiDto.toString());
            if (requestBookApiDto.getResult() == null) { // api 못받으면 에러처리(result가 null이면, 값이 들어오는
                // 필드들이 있다.)
                throw new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO);
            }
            // 가져온 책 정보를 bookInfo에 저장한다
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

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        // 국립 중앙 도서관api 이용 url
        String seojiUrl =
                "https://www.nl.go.kr/NL/search/openApi/search.do?key=" + nationalIsbnApiKey +
                        "&detailSearch=true&apiType=json&isbnOp=isbn&isbnCode=" + isbn;
        RestTemplate restTemplate = new RestTemplate();

        //국립도서관 api 호출
        ResponseEntity<String> response = restTemplate.exchange(seojiUrl, HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        String jsonString = response.getBody();  // ResponseEntity에서 JSON 문자열 가져오기
        ObjectMapper mapper = new ObjectMapper();  // ObjectMapper 객체 생성

        RequestBookApiDto requestBookApiDto = null;
        try {
            requestBookApiDto = mapper.readValue(jsonString, RequestBookApiDto.class);  // JSON 문자열을 RequestBookApiDto 객체로 변환;  // 변환된 객체에서 데이터를 가져와 출력
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        bookInfoRepo.deleteById(id);
    }
}
