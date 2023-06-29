package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.repository.BookInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookInfoService {
    private final BookInfoRepository bookInfoRepo;

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

        log.error("bookInfoList: {}", bookInfoList);
        Page<ResponseBookInfoDto> responseBookInfoDtoList = bookInfoList.map(ResponseBookInfoDto::from);
        return responseBookInfoDtoList;
    }


    @Transactional
    public ResponseBookInfoWithBookDto getBookInfoById(Long bookInfoId) {
        log.info("bookInfoId: {}", bookInfoId);// 또는 log 등을 사용하여 로그로 출력
        BookInfo bookInfo =
                bookInfoRepo.findById(bookInfoId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO_BY_ID));
        List<Book> bookList = bookInfo.getBookList();
        log.info("bookList: {}", bookList.toString());// 또는 log 등을 사용하여 로그로 출력
        return ResponseBookInfoWithBookDto.from(bookInfo);
    }

    //TODO: 책 정보만 수정할 때는 없을 것 같다(나중에 삭제)
//    @Transactional
//    public ResponseBookInfoDto updateBookInfoById(RequestBookInfoDto requestBookInfoDto) {
//        BookInfo bookInfo = new BookInfo().builder()
//                .title("title")
//                .isbn("isbn")
//                .author("author")
//                .publisher("publisher")
//                .image("image")
//                .publishedAt("publishedAt")
//                .build();
//        bookInfoRepo.save(bookInfo);
//        return null;
//    }

    @Transactional
    public void deleteBookInfoById(Long id) {
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        bookInfoRepo.deleteById(id);
    }
}
