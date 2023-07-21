package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.domain.RequestBook;
import com.hae.library.dto.Book.Request.RequestBookWithBookInfoDto;
import com.hae.library.dto.Book.Response.ResponseBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.Request.RequestBookInfoDto;
import com.hae.library.dto.Lending.Request.RequestCallsignDto;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.CategoryErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.CategoryRepository;
import com.hae.library.repository.RequestBookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final RequestBookRepository requestBookInfoRepo;

    /**
     * 새로운 책을 추가하는 메서드입니다.
     *
     * @param requestBookWithBookInfoDto 새로운 책 정보 요청 DTO
     * @return ResponseBookWithBookInfoDto 새로운 책 정보 응답 DTO
     *
     * @throws BookErrorCode 청구기호가 중복되는 경우
     */
    @Transactional
    public void createBook(RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
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
            // 구매 요청한 도서이고 아직 승인을 하지 않았으면, 도서 구매 요청을 true로 변경하고, 구매일을 추가합니다.
            RequestBook requestBook = bookInfo.getRequestBook();
            if (requestBook != null && requestBook.isApproved() == false) {
                requestBook.approve();
                requestBook.updateApprovedAt();
                requestBookInfoRepo.save(requestBook);
            }
            // 새로 생성한 BookInfo를 사용하여 책을 저장합니다.
            saveBookWithBookInfo(requestBookWithBookInfoDto, bookInfo);
        } else {
            // BookInfo가 존재하지 않는 경우 새로운 BookInfo를 생성합니다.(다른 곳에서 매개변수가 따로 쓰이기 때문에 새로 생성합니다)
            RequestBookInfoDto requestBookInfoDto = RequestBookInfoDto.builder()
                    .isbn(requestBookWithBookInfoDto.getIsbn())
                    .title(requestBookWithBookInfoDto.getTitle())
                    .author(requestBookWithBookInfoDto.getAuthor())
                    .publisher(requestBookWithBookInfoDto.getPublisher())
                    .image(requestBookWithBookInfoDto.getImage())
                    .categoryName(requestBookWithBookInfoDto.getCategoryName())
                    .publisher(requestBookWithBookInfoDto.getPublisher())
                    .publishedAt(requestBookWithBookInfoDto.getPublishedAt())
                    .build();
            BookInfo newBookInfo = bookInfoService.createBookInfo(requestBookInfoDto);
            // 새로 생성한 BookInfo를 사용하여 책을 저장합니다.
            saveBookWithBookInfo(requestBookWithBookInfoDto, newBookInfo);
        }
    }

    /**
     * createBook에 중복 되는 메서드인, book을 저장하는 메서드입니다.
     *
     * @param requestBookWithBookInfoDto 새로운 책 정보 요청 DTO
     */
    private void saveBookWithBookInfo(RequestBookWithBookInfoDto requestBookWithBookInfoDto,
                                                             BookInfo bookInfo) {
        Book book = Book.builder()
                .callSign(requestBookWithBookInfoDto.getCallSign())
                .status(BookStatus.valueOf(requestBookWithBookInfoDto.getStatus()))
                .donator(requestBookWithBookInfoDto.getDonator())
                .lendingStatus(false)
                .build();
        // 생성한 책 객체에 BookInfo를 추가합니다.
        book.addBookInfo(bookInfo);
        // 책 객체를 데이터베이스에 저장합니다.
        Book updateBook = bookRepo.save(book);
    }

    /**
     * 책을 ID로 조회하는 메서드입니다.
     *
     * @param bookId 책 ID
     * @return ResponseBookWithBookInfoDto 책 정보 응답 DTO
     *
     * @throws BookErrorCode 책을 찾을 수 없는 경우
     */
    @Transactional
    public ResponseBookWithBookInfoDto getBookById(Long bookId) {
        // 주어진 ID를 이용하여 도서를 검색합니다. 만약 도서를 찾을 수 없다면 예외를 발생시킵니다.
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        return ResponseBookWithBookInfoDto.from(book);
    }

    /**
     * 책을 청구기호로 조회하는 메서드입니다.
     *
     * @param requestCallsignDto 청구기호 요청 DTO
     * @return ResponseBookWithBookInfoDto 책 정보 응답 DTO
     *
     * @throws BookErrorCode 책을 찾을 수 없는 경우
     */
    @Transactional
    public ResponseBookWithBookInfoDto getBookByCallSign(RequestCallsignDto requestCallsignDto) {
        String callSign = requestCallsignDto.getCallsign();

        // 청구기호를 사용하여 도서를 검색합니다.
        // 만약 도서를 찾을 수 없다면 예외를 발생시킵니다.
        Book book = bookRepo.findByCallSign(callSign).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        return ResponseBookWithBookInfoDto.from(book);
    }

    /**
     * 책을 수정하는 메서드입니다.
     *
     * @param requestBookWithBookInfoDto 책 정보 요청 DTO
     * @return ResponseBookWithBookInfoDto 책 정보 응답 DTO
     *
     * @throws BookErrorCode 책을 찾을 수 없는 경우
     * @throws BookErrorCode 청구기호가 중복되는 경우
     * @throws BookErrorCode 카테고리를 찾을 수 없는 경우
     */
    public ResponseBookWithBookInfoDto updateBook(RequestBookWithBookInfoDto requestBookWithBookInfoDto) {
        // request DTO에서 ID를 얻어와 해당 ID의 도서를 찾습니다. 도서를 찾지 못하면 예외를 발생시킵니다.
        Book book = bookRepo.findById(requestBookWithBookInfoDto.getId()).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));
        BookInfo bookInfo = book.getBookInfo();

        // 청구기호 저장하기전에 중복되는지 확인합니다.
        if (bookRepo.existsByCallSignAndIdIsNot(requestBookWithBookInfoDto.getCallSign(),
                book.getId())) {
            throw new RestApiException(BookErrorCode.DUPLICATE_CALLSIGN);
        }

        // 자신을 제외한 isbn이 중복되는지 확인합니다.
        if (bookInfoRepo.existsByIsbnAndIdIsNot(requestBookWithBookInfoDto.getIsbn(),
                bookInfo.getId())) {
            throw new RestApiException(BookErrorCode.DUPLICATE_ISBN);
        }

        // 카테고리를 조회하고 카테고리가 존재하지 않는다면 예외를 발생시킵니다.
        Category category =
                categoryRepo.findByCategoryName(requestBookWithBookInfoDto.getCategoryName()).orElseThrow(() -> new RestApiException(CategoryErrorCode.BAD_REQUEST_CATEGORY));


        // 도서 정보를 업데이트하고, 업데이트된 도서 정보를 데이터베이스에 저장합니다.
        bookInfo.updateBookInfo(requestBookWithBookInfoDto.getTitle(),
                requestBookWithBookInfoDto.getIsbn(), requestBookWithBookInfoDto.getAuthor(),
                requestBookWithBookInfoDto.getPublisher(),
                requestBookWithBookInfoDto.getPublishedAt(),
                requestBookWithBookInfoDto.getImage(), category);
        bookInfoRepo.save(bookInfo);

        // 청구기호와 도서의 상태 그리고 기증자 정보를 업데이트하고, 업데이트된 도서를 데이터베이스에 저장합니다.
        book.updateBook(requestBookWithBookInfoDto.getCallSign(),
                BookStatus.valueOf(requestBookWithBookInfoDto.getStatus()), requestBookWithBookInfoDto.getDonator());
        bookRepo.save(book);

        return ResponseBookWithBookInfoDto.from(book);
    }

    /**
     * 책을 삭제하는 메서드입니다.
     *
     * @param bookId 책 ID
     *
     * @throws BookErrorCode 책을 찾을 수 없는 경우
     * @throws BookErrorCode 대여중인 책을 삭제하려고 하는 경우
     */
    public void deleteBookById(Long bookId) {
        // 삭제할 도서를 ID를 이용하여 검색합니다. 도서를 찾지 못하면 예외를 발생시킵니다.
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));

        // 도서가 대여중인지 확인합니다.
        if (book.isLendingStatus() == true) {
            throw new RestApiException(BookErrorCode.NOT_DELETE_BECAUSE_RENT_BOOK);
        }

        // 마지막 도서이면 BookInfo도 삭제합니다.
        if (bookRepo.countByBookInfo(book.getBookInfo()) == 1) {
            bookInfoRepo.delete(book.getBookInfo());
        }

        // 찾은 도서를 데이터베이스에서 삭제합니다.
        bookRepo.delete(book);
    }
}
