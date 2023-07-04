package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.Book.ResponseBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepo;
    private final BookInfoRepository bookInfoRepo;
    private final BookInfoService bookInfoService;
    private final CategoryRepository categoryRepo;

    // 새로운 책을 생성하는 메서드입니다.
    @Transactional
    public ResponseBookWithBookInfoDto createBook(RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        log.error("requestBookDto: {}", requestBookWithBookInfoDto);

        // 청구기호가 중복되는지 확인합니다. 중복되는 경우 예외 처리합니다.
        if (bookRepo.existsByCallSign(requestBookWithBookInfoDto.getCallSign())) {
            throw new RestApiException(BookErrorCode.DUPLICATE_BOOK);
        }

        // ISBN을 가지고 있는 BookInfo 객체를 찾습니다.
        Optional<BookInfo> bookInfoOptional = bookInfoRepo.findByIsbn(requestBookWithBookInfoDto.getIsbn());

        ResponseBookWithBookInfoDto responseBookWithBookInfoDto;
        // BookInfo가 존재하는 경우와 존재하지 않는 경우 다르게 처리합니다.
        if (bookInfoOptional.isPresent()) {
            // BookInfo가 존재하는 경우 기존 BookInfo를 사용합니다.
            BookInfo bookInfo = bookInfoOptional.get();

            responseBookWithBookInfoDto =
                    saveBookWithBookInfo(requestBookWithBookInfoDto,
                            bookInfo);
        } else {
            // BookInfo가 존재하지 않는 경우 새로운 BookInfo를 생성합니다.
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
            // 새로 생성한 BookInfo를 사용하여 책을 저장합니다.
            responseBookWithBookInfoDto =
                    saveBookWithBookInfo(requestBookWithBookInfoDto, bookInfo);
        }
        return responseBookWithBookInfoDto;
    }

    // 책과 BookInfo를 함께 저장하는 메서드입니다.
    private ResponseBookWithBookInfoDto saveBookWithBookInfo(RequestBookWithBookInfoDto requestBookWithBookInfoDto,
                                                             BookInfo bookInfo) {
        Book book = Book.builder()
                .callSign(requestBookWithBookInfoDto.getCallSign())
                .status(BookStatus.valueOf(requestBookWithBookInfoDto.getStatus()))
                .donator(requestBookWithBookInfoDto.getDonator())
                .build();
        // 생성한 책 객체에 BookInfo를 추가합니다.
        book.addBookInfo(bookInfo);
        // 책 객체를 데이터베이스에 저장합니다.

        Book updateBook = bookRepo.save(book);
        return ResponseBookWithBookInfoDto.from(updateBook);
    }


    // 모든 책을 조회하는 메서드입니다.
    @Transactional
    public Page<ResponseBookWithBookInfoDto> getAllBook(String search, int page, int size) {
        // 페이징 정보를 설정합니다.
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        // Specification을 이용해 동적 쿼리를 생성합니다.
        Specification<Book> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction(); // 모든 결과를 반환합니다.
            }
            // 검색어가 포함된 경우 해당 결과를 반환합니다.
            return cb.or(
                    cb.like(cb.lower(root.get("bookInfo").get("title")),
                            "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("callSign")), "%" + search.toLowerCase() + "%")
            );
        };

        // 위에서 생성한 쿼리를 실행하여 책 리스트를 얻어옵니다.
        Page<Book> bookList = bookRepo.findAll(spec, pageable);
        Page<ResponseBookWithBookInfoDto> responseBookWithBookInfoDtoList = bookList.map(ResponseBookWithBookInfoDto::from);
        return responseBookWithBookInfoDtoList;
    }

    // 책을 ID로 조회하는 메서드입니다.
    @Transactional
    public ResponseBookWithBookInfoDto getBookById(Long bookId) {
        // 주어진 ID를 이용하여 도서를 검색합니다. 만약 도서를 찾을 수 없다면 예외를 발생시킵니다.
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        return ResponseBookWithBookInfoDto.from(book);
    }

    // 책을 청구기호로 조회하는 메서드입니다.
    @Transactional
    public ResponseBookWithBookInfoDto getBookByCallSign(String callSign) {
        // 청구기호를 사용하여 도서를 검색합니다. 만약 도서를 찾을 수 없다면 예외를 발생시킵니다.
        Book book = bookRepo.findByCallSign(callSign).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        return ResponseBookWithBookInfoDto.from(book);
    }

    // 책을 수정하는 메서드입니다.
    public ResponseBookWithBookInfoDto updateBook(RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        // request DTO에서 ID를 얻어와 해당 ID의 도서를 찾습니다. 도서를 찾지 못하면 예외를 발생시킵니다.
        Book book = bookRepo.findById(requestBookWithBookInfoDto.getId()).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        BookInfo bookInfo = book.getBookInfo();

        // TODO: 청구기호 저장하기전에 중복되는지 확인
        if (bookRepo.existsByCallSign(requestBookWithBookInfoDto.getCallSign())) {
            throw new RestApiException(BookErrorCode.DUPLICATE_CALLSIGN);
        }

        // 도서 정보를 업데이트하고, 업데이트된 도서 정보를 데이터베이스에 저장합니다.
        bookInfo.updateBookInfo(requestBookWithBookInfoDto.getTitle(), requestBookWithBookInfoDto.getIsbn(), requestBookWithBookInfoDto.getAuthor(),
                requestBookWithBookInfoDto.getPublisher(), requestBookWithBookInfoDto.getPublishedAt(), requestBookWithBookInfoDto.getImage());
        bookInfoRepo.save(bookInfo);

        // 청구기호와 도서의 상태 그리고 기증자 정보를 업데이트하고, 업데이트된 도서를 데이터베이스에 저장합니다.
        book.updateBook(requestBookWithBookInfoDto.getCallSign(),
                BookStatus.valueOf(requestBookWithBookInfoDto.getStatus()), requestBookWithBookInfoDto.getDonator());
        bookRepo.save(book);

        return ResponseBookWithBookInfoDto.from(book);
    }

    // 책을 삭제하는 메서드입니다.
    public void deleteBookById(Long bookId) {
        // 삭제할 도서를 ID를 이용하여 검색합니다. 도서를 찾지 못하면 예외를 발생시킵니다.
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));

        // TODO: 도서가 대여중인지 확인
        // TODO: 마지막 도서이면 BookInfo도 삭제

        // 찾은 도서를 데이터베이스에서 삭제합니다.
        bookRepo.deleteById(bookId);
    }

}
