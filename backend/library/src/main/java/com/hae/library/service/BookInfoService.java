package com.hae.library.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.dto.Book.RequestBookApiDto;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.repository.BookInfoRepository;
import lombok.AllArgsConstructor;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BookInfoService {
    private final BookInfoRepository bookInfoRepo;
    public BookInfoService(BookInfoRepository bookInfoRepo) {
        this.bookInfoRepo = bookInfoRepo;
    }

    @Value("${nationalIsbnApiKey}")
    private String nationalIsbnApiKey;


    // bookService.createBook에 쓰인다
    @Transactional
    public ResponseBookInfoDto createBookInfo(RequestBookWithBookInfoDto requestBookDto) {
        BookInfo bookInfo = BookInfo.builder()
                .title(requestBookDto.getTitle())
                .author(requestBookDto.getAuthor())
                .isbn(requestBookDto.getIsbn())
                .image(requestBookDto.getImage())
                .publisher(requestBookDto.getPublisher())
                .publishedAt(requestBookDto.getPublishedAt())
                .build();

        BookInfo newBookInfo = bookInfoRepo.save(bookInfo);
        return ResponseBookInfoDto.from(newBookInfo);
    }

    @Transactional
    public Page<ResponseBookInfoDto> getAllBookInfo(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        // Specification을 이용해 동적 쿼리 생성
        Specification<BookInfo> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction(); // 모든 결과 반환
            }
            // 검색어가 포함된 경우 해당 결과 반환
            return cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%");
        };

        Page<BookInfo> bookInfoList = bookInfoRepo.findAll(spec, pageable);

        log.error("bookInfoList: {}", bookInfoList.toList());
        Page<ResponseBookInfoDto> responseBookInfoDtoList = bookInfoList.map(ResponseBookInfoDto::from);
        return responseBookInfoDtoList;
    }


    @Transactional
    public ResponseBookInfoWithBookDto getBookInfoById(Long bookInfoId) {
        log.info("bookInfoId: {}", bookInfoId);// 또는 log 등을 사용하여 로그로 출력
        BookInfo bookInfo =
                bookInfoRepo.findById(bookInfoId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO));
        List<Book> bookList = bookInfo.getBookList();
        log.info("bookList: {}", bookList.toString());// 또는 log 등을 사용하여 로그로 출력
        return ResponseBookInfoWithBookDto.from(bookInfo);
    }

    @Transactional
    public ResponseBookInfoWithBookDto getBookInfoByIsbn(String isbn) {
        // 만약 책 정보가 없다면 국립중앙도서관 API로 책 정보를 가져온다
        Optional<BookInfo> bookInfoOptional = bookInfoRepo.findByIsbn(isbn);
        BookInfo bookInfo;
        if (bookInfoOptional.isEmpty()) {
            // 국립중앙도서관 API로 책 정보를 가져온다
            RequestBookApiDto requestBookApiDto = searchByIsbn(isbn);
            if (requestBookApiDto == null) { // api 못받으면 에러처리
                throw new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO);
            }
            // 가져온 책 정보를 bookInfo에 저장한다
            return requestBookApiDto.toResponseBookInfoWithBookDto();
        } else { // 책 정보가 있다면 그대로 가져온다
            bookInfo = bookInfoOptional.get();
        }
        return ResponseBookInfoWithBookDto.from(bookInfo);
    }

    public RequestBookApiDto searchByIsbn(String isbn) {
        //컨트롤러에서 isbn을 받아온다 - 받아온 isbn 호출
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
//        log.warn("responseEntity : {}",response.getBody());

        //Dto 인스턴스화
//        RequestBookApiDto bookApi = response.getBody();

//        log.warn("bookApi : {}",bookApi.toString());



//        if (bookApi != null) {
//            // DTO setting
//            bookApi.builder()
//                    .bookName(bookApi.getBookName())
//                    .bookPrice(bookApi.getBookPrice())
//                    .seriesNo(bookApi.getSeriesNo())
//                    .category(bookApi.getCategory())
//                    .writer(bookApi.getWriter())
//                    .publisher(bookApi.getPublisher())
//                    .bookDescription(bookApi.getBookDescription())
//                    .bookImageURL(bookApi.getBookImageURL())
//                    .bookPublishDate(bookApi.getBookPublishDate())
//                    .build();
//        }

        //데이터 담은 객체 리턴
        return requestBookApiDto;
    }

    @Transactional
    public void deleteBookInfoById(Long id) {
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        bookInfoRepo.deleteById(id);
    }
}
