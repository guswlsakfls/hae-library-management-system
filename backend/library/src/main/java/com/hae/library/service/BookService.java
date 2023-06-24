package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.Book.RequestBookDto;
import com.hae.library.dto.Book.ResponseBookDto;
import com.hae.library.dto.Book.ResponseBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepo;
    private final BookInfoRepository bookInfoRepo;
    private final BookInfoService bookInfoService;

    @Transactional
    public void createBook(RequestBookDto requestBookDto) {
        log.error("requestBookDto: {}", requestBookDto);
        if (bookRepo.existsByCallSign(requestBookDto.getCallSign())) {
            throw new RestApiException(BookErrorCode.DUPLICATE_BOOK);
        }

        BookInfo bookInfo = bookInfoRepo.findByIsbn(requestBookDto.getIsbn());
        if (bookInfo == null) {
            ResponseBookInfoDto responseBookInfo = bookInfoService.createBookInfo(requestBookDto);
            bookInfo = BookInfo.builder()
                    .id(responseBookInfo.getId())
                    .title(responseBookInfo.getTitle())
                    .author(responseBookInfo.getAuthor())
                    .isbn(responseBookInfo.getIsbn())
                    .image(responseBookInfo.getImage())
                    .publisher(responseBookInfo.getPublisher())
                    .publishedAt(responseBookInfo.getPublishedAt())
                    .build();
        }

        Book book = Book.builder()
//                .bookInfo(bookInfo)
                .callSign(requestBookDto.getCallSign())
                .status(BookStatus.valueOf(requestBookDto.getStatus()))
                .donator(requestBookDto.getDonator())
                .build();
//        bookInfo.changeBookList(book);
        book.addBookInfo(bookInfo);
        bookRepo.save(book);
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
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_FOUND_BOOK));
        return ResponseBookWithBookInfoDto.from(book);
    }

    public ResponseBookWithBookInfoDto updateBookById(RequestBookDto requestBook) {
        log.error("updateBookById", requestBook);
        // 도서 ID로 도서 업데이트 로직 구현
        Book book = bookRepo.findById(requestBook.getId()).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_FOUND_BOOK));
        BookInfo bookInfo = book.getBookInfo();

        bookInfo.updateBookInfo(requestBook.getTitle(), requestBook.getIsbn(), requestBook.getAuthor(),
                requestBook.getPublisher(), requestBook.getPublishedAt(), requestBook.getImage());
        bookInfoRepo.save(bookInfo);

        book.updateBook(requestBook.getCallSign(),
                BookStatus.valueOf(requestBook.getStatus()), requestBook.getDonator());
        bookRepo.save(book);

        return ResponseBookWithBookInfoDto.from(book);
    }

    public void deleteBookById(Long bookId) {
        Book book =
                bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_FOUND_BOOK));
        bookRepo.deleteById(bookId);
    }


}
