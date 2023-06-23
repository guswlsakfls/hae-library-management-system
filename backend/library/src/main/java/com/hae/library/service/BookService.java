package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.Book.RequestBookDto;
import com.hae.library.dto.Book.ResponseBookDto;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.CommonErrorCode;
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

    @Transactional
    public void createBook(RequestBookDto requestBookDto) {
        log.error("requestBookDto: {}", requestBookDto);
        if (bookRepo.existsByCallSign(requestBookDto.getCallSign())) {
            throw new RestApiException(BookErrorCode.DUPLICATE_BOOK);
        }

        BookInfo bookInfo = bookInfoRepo.findByIsbn(requestBookDto.getIsbn());
        if (bookInfo == null) {
            bookInfo = createBookInfo(requestBookDto);
        }

        Book book = Book.builder()
                .bookInfo(bookInfo)
                .callSign(requestBookDto.getCallSign())
                .status(BookStatus.valueOf(requestBookDto.getStatus()))
                .donator(requestBookDto.getDonator())
                .build();

        bookRepo.save(book);
    }

    private BookInfo createBookInfo(RequestBookDto requestBookDto) {
        BookInfo bookInfo = BookInfo.builder()
                .title(requestBookDto.getTitle())
                .author(requestBookDto.getAuthor())
                .isbn(requestBookDto.getIsbn())
                .image(requestBookDto.getImage())
                .publisher(requestBookDto.getPublisher())
                .publishedAt(requestBookDto.getPublishedAt())
                .build();

        bookInfoRepo.save(bookInfo);
        return bookInfo;
    }

    @Transactional
    public List<ResponseBookDto> getAllBook() {
        List<Book> bookList= bookRepo.findAll();
        List<ResponseBookDto> responseBookDtoList = bookList.stream()
                .map(ResponseBookDto::from)
                .collect(Collectors.toList());
        return responseBookDtoList;
    }

    @Transactional
    public ResponseBookDto getBookById(Long bookId) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_FOUND_BOOK));
        return ResponseBookDto.from(book);
    }

    public Book updateBookById(RequestBookDto requestBook) {
        log.error("updateBookById", requestBook);
        // 도서 ID로 도서 업데이트 로직 구현
        Book book =
                bookRepo.findById(requestBook.getId()).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_FOUND_BOOK));
        BookInfo bookInfo = book.getBookInfo();

        bookInfo.updateBookInfo(requestBook.getTitle(), requestBook.getIsbn(), requestBook.getAuthor(),
                requestBook.getPublisher(), requestBook.getPublishedAt(), requestBook.getImage());
        bookInfoRepo.save(bookInfo);

        book.updateBook(requestBook.getCallSign(),
                BookStatus.valueOf(requestBook.getStatus()), requestBook.getDonator());
        return bookRepo.save(book);
    }

    public void deleteBookById(Long bookId) {
        Book book =
                bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.NOT_FOUND_BOOK));
        bookRepo.deleteById(bookId);
    }


}
