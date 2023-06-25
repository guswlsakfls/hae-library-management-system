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
    public List<ResponseBookInfoDto> getAllBookInfo() {
        List<BookInfo> bookInfoList = bookInfoRepo.findAll();
        log.error("bookInfoList: {}", bookInfoList);
        List<ResponseBookInfoDto> responseBookInfoDtoList = bookInfoList.stream()
                .map(ResponseBookInfoDto::from)
                .collect(Collectors.toList());
        return responseBookInfoDtoList;
    }

    @Transactional
    public ResponseBookInfoWithBookDto getBookInfoById(Long bookInfoId) {
        log.info("bookInfoId: {}", bookInfoId);// 또는 log 등을 사용하여 로그로 출력
        BookInfo bookInfo =
                bookInfoRepo.findById(bookInfoId).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_FOUND_BOOKINFO_BY_ID));
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
        BookInfo bookInfo = bookInfoRepo.findById(id).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_FOUND_BOOK));
        bookInfoRepo.deleteById(id);
    }
}
