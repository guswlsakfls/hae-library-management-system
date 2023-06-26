package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.Book.ResponseBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepo;
    private final BookInfoRepository bookInfoRepo;
    private final BookInfoService bookInfoService;

    @Transactional
    public ResponseBookWithBookInfoDto createBook(RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        log.error("requestBookDto: {}", requestBookWithBookInfoDto);
        if (bookRepo.existsByCallSign(requestBookWithBookInfoDto.getCallSign())) {
            throw new RestApiException(BookErrorCode.DUPLICATE_BOOK);
        }

        Optional<BookInfo> bookInfoOptional = bookInfoRepo.findByIsbn(requestBookWithBookInfoDto.getIsbn());

        ResponseBookWithBookInfoDto responseBookWithBookInfoDto;
        if (bookInfoOptional.isPresent()) {
            // BookInfo가 존재하는 경우
            BookInfo bookInfo = bookInfoOptional.get();
            responseBookWithBookInfoDto =
                    saveBookWithBookInfo(requestBookWithBookInfoDto,
                    bookInfo);
        } else {
            // BookInfo가 존재하지 않는 경우
            ResponseBookInfoDto responseBookInfo = bookInfoService.createBookInfo(requestBookWithBookInfoDto);
            BookInfo bookInfo = BookInfo.builder()
                    .id(responseBookInfo.getId())
                    .title(responseBookInfo.getTitle())
                    .author(responseBookInfo.getAuthor())
                    .isbn(responseBookInfo.getIsbn())
                    .image(responseBookInfo.getImage())
                    .publisher(responseBookInfo.getPublisher())
                    .publishedAt(responseBookInfo.getPublishedAt())
                    .build();
            responseBookWithBookInfoDto =
                    saveBookWithBookInfo(requestBookWithBookInfoDto, bookInfo);
        }

        return responseBookWithBookInfoDto;
    }

    private ResponseBookWithBookInfoDto saveBookWithBookInfo(RequestBookWithBookInfoDto requestBookWithBookInfoDto,
                                                             BookInfo bookInfo) {
        Book book = Book.builder()
                .callSign(requestBookWithBookInfoDto.getCallSign())
                .status(BookStatus.valueOf(requestBookWithBookInfoDto.getStatus()))
                .donator(requestBookWithBookInfoDto.getDonator())
                .build();
        book.addBookInfo(bookInfo);
        Book updateBook = bookRepo.save(book);
        return ResponseBookWithBookInfoDto.from(updateBook);
    }

    @Transactional
    public List<ResponseBookWithBookInfoDto> getAllBook() {
        List<Book> bookList= bookRepo.findAll();
        List<ResponseBookWithBookInfoDto> responseBookWithBookInfoDtoList = bookList.stream()
                .map(ResponseBookWithBookInfoDto::from)
                .collect(Collectors.toList());
        return responseBookWithBookInfoDtoList;
    }

    @Transactional
    public ResponseBookWithBookInfoDto getBookById(Long bookId) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        return ResponseBookWithBookInfoDto.from(book);
    }

    public ResponseBookWithBookInfoDto updateBook(RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        log.error("updateBookById", requestBookWithBookInfoDto);
        // 도서 ID로 도서 업데이트 로직 구현
        Book book = bookRepo.findById(requestBookWithBookInfoDto.getId()).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        BookInfo bookInfo = book.getBookInfo();

        bookInfo.updateBookInfo(requestBookWithBookInfoDto.getTitle(), requestBookWithBookInfoDto.getIsbn(), requestBookWithBookInfoDto.getAuthor(),
                requestBookWithBookInfoDto.getPublisher(), requestBookWithBookInfoDto.getPublishedAt(), requestBookWithBookInfoDto.getImage());
        bookInfoRepo.save(bookInfo);

        book.updateBook(requestBookWithBookInfoDto.getCallSign(),
                BookStatus.valueOf(requestBookWithBookInfoDto.getStatus()), requestBookWithBookInfoDto.getDonator());
        bookRepo.save(book);

        return ResponseBookWithBookInfoDto.from(book);
    }

    public void deleteBookById(Long bookId) {
        Book book =
                bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        bookRepo.deleteById(bookId);
    }
}
