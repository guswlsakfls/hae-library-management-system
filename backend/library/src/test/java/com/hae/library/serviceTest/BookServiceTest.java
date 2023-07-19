package com.hae.library.serviceTest;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.Book.Request.RequestBookWithBookInfoDto;
import com.hae.library.dto.Book.Response.ResponseBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.Request.RequestBookInfoDto;
import com.hae.library.dto.Lending.Request.RequestCallsignDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.CategoryRepository;
import com.hae.library.service.BookInfoService;
import com.hae.library.service.BookService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepo;

    @Mock
    private BookInfoRepository bookInfoRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private BookInfoService bookInfoService;

    @InjectMocks
    private BookService bookService;

    private RequestBookWithBookInfoDto requestBookWithBookInfoDto;
    private Book mockBook;
    private Long existingBookId;
    private String callSign;
    private String donator;
    private String status;

    @BeforeEach
    public void setup() {
        existingBookId = 1L;  // 존재하는 책 ID로 가정
        callSign = "12345";
        donator = "John Doe";
        status = "FINE";

        requestBookWithBookInfoDto = RequestBookWithBookInfoDto.builder()
                .id(existingBookId)
                .callSign(callSign)
                .status(status)
                .donator(donator)
                .title("Java Programming")
                .author("John Smith")
                .isbn("9781234567890")
                .image("book-image.jpg")
                .publisher("ABC Publishing")
                .publishedAt("2022-01-01")
                .categoryName("총류")
                .build();

        mockBook = Book.builder()
                .id(existingBookId)
                .callSign(callSign)
                .status(BookStatus.valueOf(status))
                .donator(donator)
                .lendingStatus(false)
                .bookInfo(new BookInfo())  // 필요한 BookInfo 정보 설정
                .build();
    }

    @Nested
    @DisplayName("책 추가")
    public class CreateBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("BookInfo가 이미 존재하는 경우 책 추가")
            public void createBookWithExistingBookInfoTest() {
                // Given
                BookInfo newBookInfo = BookInfo.builder()
                        .title(requestBookWithBookInfoDto.getTitle())
                        .author(requestBookWithBookInfoDto.getAuthor())
                        .isbn(requestBookWithBookInfoDto.getIsbn())
                        .image(requestBookWithBookInfoDto.getImage())
                        .publisher(requestBookWithBookInfoDto.getPublisher())
                        .publishedAt(requestBookWithBookInfoDto.getPublishedAt())
                        .category(Category.builder().categoryName(requestBookWithBookInfoDto.getCategoryName()).build())
                        .build();
                // callSign이 존재하지 않는 경우
                when(bookRepo.existsByCallSign(requestBookWithBookInfoDto.getCallSign())).thenReturn(false);
                // bookInfo가 존재하는 경우
                when(bookInfoRepo.findByIsbn(requestBookWithBookInfoDto.getIsbn())).thenReturn(Optional.of(newBookInfo));

                // When
                bookService.createBook(requestBookWithBookInfoDto);

                // Then
                //
                verify(bookRepo, times(1)).save(any(Book.class));
                // bookRepo.save()가 호출되었는지 확인
                Assertions.assertTrue(newBookInfo.getBookList().size() == 1);
            }

            @Test
            @DisplayName("BookInfo가 존재하지 않는 경우 책 추가")
            public void createBookWithNewBookInfoTest() {
                // Given
                Category mockCategory = Category.builder()
                        .categoryName(requestBookWithBookInfoDto.getCategoryName())
//                        .bookInfoList(new ArrayList<>())
                        .build();

                BookInfo newBookInfo = BookInfo.builder()
                        .title(requestBookWithBookInfoDto.getTitle())
                        .author(requestBookWithBookInfoDto.getAuthor())
                        .isbn(requestBookWithBookInfoDto.getIsbn())
                        .image(requestBookWithBookInfoDto.getImage())
                        .publisher(requestBookWithBookInfoDto.getPublisher())
                        .publishedAt(requestBookWithBookInfoDto.getPublishedAt())
                        .bookList(new ArrayList<>())
                        .category(mockCategory)
                        .build();

                // callSign이 존재하지 않는 경우
                when(bookRepo.existsByCallSign(requestBookWithBookInfoDto.getCallSign())).thenReturn(false);
                // bookInfo가 존재하지 않는 경우
                when(bookInfoRepo.findByIsbn(requestBookWithBookInfoDto.getIsbn())).thenReturn(Optional.empty());
                // 도서 정보가 저장되어 반환 되는 경우
                when(bookInfoService.createBookInfo(any(RequestBookInfoDto.class))).thenReturn(newBookInfo);

                // When
                bookService.createBook(requestBookWithBookInfoDto);

                // Then
                verify(bookRepo, times(1)).save(any(Book.class));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("청구기호가 이미 존재하는 경우 예외 발생")
            public void createBookWithExistingCallSignTest() {
                // Given
                // callSign이 존재하는 경우
                when(bookRepo.existsByCallSign(requestBookWithBookInfoDto.getCallSign())).thenReturn(true);

                // When & Then
                assertThrows(RestApiException.class, () -> {
                    bookService.createBook(requestBookWithBookInfoDto);
                });
            }
        }
    }

    @Nested
    @DisplayName("도서 조회")
    public class GetBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("ID로 책 조회")
            public void getBookByValidIdTest() {
                // Given
                Long validBookId = 1L;  // 존재하는 책 ID로 가정

                Book mockBook = Book.builder()
                                .id(validBookId)
                                .callSign("12345")
                                .status(BookStatus.valueOf("FINE"))
                                .donator("John Doe")
                                .lendingStatus(false)
                                .bookInfo(BookInfo.builder()
                                        .title("Java Programming")
                                        .author("John Smith")
                                        .isbn("9781234567890")
                                        .image("book-image.jpg")
                                        .publisher("ABC Publishing")
                                        .publishedAt("2022-01-01")
                                        .category(Category.builder().categoryName("총류").build())
                                        .build())
                                .build();
                mockBook.setCreatedAt(LocalDateTime.now());
                mockBook.setUpdatedAt(LocalDateTime.now());

                ResponseBookWithBookInfoDto expectedResponse =
                        ResponseBookWithBookInfoDto.from(mockBook);

                when(bookRepo.findById(validBookId)).thenReturn(Optional.of(mockBook));

                // When
                ResponseBookWithBookInfoDto actualResponse = bookService.getBookById(validBookId);

                // Then
                // 모든 필드를 비교합니다.
                assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
            }

            @Test
            @DisplayName("청구기호로 책 조회")
            public void getBookByValidCallSignTest() {
                // Given
                RequestCallsignDto requestCallsignDto = RequestCallsignDto.builder()
                        .callsign("12345")
                        .build();


                Book mockBook = Book.builder()
                        .id(1L)
                        .callSign(requestCallsignDto.getCallsign())
                        .status(BookStatus.valueOf("FINE"))
                        .donator("John Doe")
                        .lendingStatus(false)
                        .bookInfo(BookInfo.builder()
                                .title("Java Programming")
                                .author("John Smith")
                                .isbn("9781234567890")
                                .image("book-image.jpg")
                                .publisher("ABC Publishing")
                                .publishedAt("2022-01-01")
                                .category(Category.builder().categoryName("총류").build())
                                .build())
                        .build();
                mockBook.setCreatedAt(LocalDateTime.now());
                mockBook.setUpdatedAt(LocalDateTime.now());

                ResponseBookWithBookInfoDto expectedResponse = ResponseBookWithBookInfoDto.from(mockBook);

                when(bookRepo.findByCallSign(requestCallsignDto.getCallsign())).thenReturn(Optional.of(mockBook));

                // When
                ResponseBookWithBookInfoDto actualResponse = bookService.getBookByCallSign(requestCallsignDto);

                // Then
                assertThat(actualResponse).isNotNull();
                assertThat(expectedResponse).isNotNull();

                assertThat(actualResponse.getId()).isEqualTo(expectedResponse.getId());
                assertThat(actualResponse.getCallSign()).isEqualTo(expectedResponse.getCallSign());
                assertThat(actualResponse.getStatus()).isEqualTo(expectedResponse.getStatus());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("존재하지 않는 책 ID로 조회하는 경우")
            public void getBookByInvalidIdTest() {
                // Given
                Long invalidBookId = 100L;  // 존재하지 않는 책 ID로 가정

                when(bookRepo.findById(invalidBookId)).thenReturn(Optional.empty());

                // When & Then
                assertThrows(RestApiException.class, () -> {
                    bookService.getBookById(invalidBookId);
                });
            }

            @Test
            @DisplayName("잘못된 청구기호로 도서 조회")
            public void getBookByInvalidCallSignTest() {
                // Given
                RequestCallsignDto requestCallsignDto = RequestCallsignDto.builder()
                        .callsign("invalidCallSign")
                        .build();

                when(bookRepo.findByCallSign(requestCallsignDto.getCallsign())).thenReturn(Optional.empty());

                // When & Then
                assertThrows(RestApiException.class, () -> bookService.getBookByCallSign(requestCallsignDto));
            }
        }
    }

    @Nested
    @DisplayName("책 수정")
    public class UpdateBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("책 및 책 정보 수정")
            public void updateBookTest() {
                // Given
                Long existingBookId = 1L;  // 존재하는 책 ID로 가정
                String callSign = "12345";
                String donator = "John Doe";
                String status = "FINE"; // "FINE", FINE 문제 (string으로 받아서 enum으로 변환하는 로직이 있어야 함)

                RequestBookWithBookInfoDto requestDto = requestBookWithBookInfoDto;
                // 기타 필요한 BookInfo 정보를 requestDto에 설정

                Book book = Book.builder()
                        .id(existingBookId)
                        .callSign(callSign)
                        .status(BookStatus.valueOf(status))
                        .donator(donator)
                        .lendingStatus(false)
                        .bookInfo(new BookInfo())  // 기타 필요한 BookInfo 정보 설정
                        .build();
                book.setCreatedAt(LocalDateTime.now());
                book.setUpdatedAt(LocalDateTime.now());

                when(bookRepo.findById(existingBookId)).thenReturn(Optional.of(book));
                when(bookRepo.existsByCallSignAndIdIsNot(callSign, existingBookId)).thenReturn(false);
                when(categoryRepo.findByCategoryName(any())).thenReturn(Optional.of(Category.builder().categoryName("총류").build()));
                when(bookRepo.save(any(Book.class))).thenReturn(book);
                when(bookInfoRepo.save(any(BookInfo.class))).thenReturn(book.getBookInfo());

                // When
                ResponseBookWithBookInfoDto actualResponse = bookService.updateBook(requestDto);

                // Then
                assertThat(actualResponse.getId()).isEqualTo(existingBookId);
                assertThat(actualResponse.getCallSign()).isEqualTo(callSign);
                assertThat(actualResponse.getDonator()).isEqualTo(donator);
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("책을 찾을 수 없는 경우 예외를 반환")
            public void updateBookNotFoundTest() {
                // Given
                when(bookRepo.findById(anyLong())).thenReturn(Optional.empty());

                // Then
                Assertions.assertThrows(RestApiException.class, () -> {
                    // When
                    bookService.updateBook(requestBookWithBookInfoDto);
                });
            }

            @Test
            @DisplayName("청구기호가 중복되는 경우 예외를 반환")
            public void updateBookDuplicateCallSignTest() {
                // Given
                when(bookRepo.findById(anyLong())).thenReturn(Optional.of(mockBook));
                when(bookRepo.existsByCallSignAndIdIsNot(anyString(), anyLong())).thenReturn(true);

                // Then
                Assertions.assertThrows(RestApiException.class, () -> {
                    // When
                    bookService.updateBook(requestBookWithBookInfoDto);
                });
            }

            @Test
            @DisplayName("카테고리를 찾을 수 없는 경우 예외를 반환")
            public void updateBookCategoryNotFoundTest() {
                // Given
                when(bookRepo.findById(anyLong())).thenReturn(Optional.of(mockBook));
                when(bookRepo.existsByCallSignAndIdIsNot(anyString(), anyLong())).thenReturn(false);
                when(categoryRepo.findByCategoryName(any())).thenReturn(Optional.empty());

                // Then
                Assertions.assertThrows(RestApiException.class, () -> {
                    // When
                    bookService.updateBook(requestBookWithBookInfoDto);
                });
            }
        }
    }

    @Nested
    @DisplayName("책 삭제")
    public class DeleteBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("id로 책 삭제")
            public void deleteBookTest() {
                // Given
                Long existingBookId = 1L;  // 존재하는 책 ID로 가정

                Book mockBook = Book.builder()
                        .id(existingBookId)
                        .callSign("12345")
                        .status(BookStatus.FINE)
                        .donator("John Doe")
                        .lendingStatus(false)
                        .bookInfo(new BookInfo())  // 기타 필요한 BookInfo 정보 설정
                        .build();
                mockBook.setCreatedAt(LocalDateTime.now());
                mockBook.setUpdatedAt(LocalDateTime.now());

                when(bookRepo.findById(existingBookId)).thenReturn(Optional.of(mockBook));

                // When
                bookService.deleteBookById(existingBookId);

                // Then
                verify(bookRepo, times(1)).deleteById(existingBookId);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("id에 해당하는 책을 찾을 수 없는 경우 예외를 반환")
            public void deleteBookNotFoundTest() {
                // Given
                when(bookRepo.findById(anyLong())).thenReturn(Optional.empty());

                // Then
                Assertions.assertThrows(RestApiException.class, () -> {
                    // When
                    bookService.deleteBookById(anyLong());
                });
            }

            @Test
            @DisplayName("책이 대여중울 때 삭제하려는 경우 예외를 반환")
            public void deleteBookInUseTest() {
                // Given
                Long existingBookId = 1L;  // 존재하는 책 ID로 가정

                Book mockBook = Book.builder()
                        .id(existingBookId)
                        .callSign("12345")
                        .lendingStatus(true)
                        .donator("John Doe")
                        .bookInfo(new BookInfo())  // 기타 필요한 BookInfo 정보 설정
                        .build();
                mockBook.setCreatedAt(LocalDateTime.now());
                mockBook.setUpdatedAt(LocalDateTime.now());

                when(bookRepo.findById(existingBookId)).thenReturn(Optional.of(mockBook));

                // Then
                Assertions.assertThrows(RestApiException.class, () -> {
                    // When
                    bookService.deleteBookById(existingBookId);
                });
            }
        }
    }
}
