package com.hae.library.controllerTest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.CommonErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("BookController 통합 테스트")
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private String token;

    private Long categoryId;

    private Long bookId;
    private Book book;

    @BeforeEach
    public void setup() throws Exception {
        String authContent = "{\"email\":\"admin@gmail.com\", \"password\":\"ehdaud11!\"}";

        // 로그인하여 토큰을 발급받습니다.
        String loginResponse = mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authContent))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(loginResponse);
        this.token = responseJson.get("data").get("accessToken").asText();

        // 테스트용 카테고리를 등록합니다.
        Category category = categoryRepository.save(Category.builder()
                .categoryName("테스트용")
                .build());

        System.out.println(category);
        this.categoryId = category.getId();

        // 테스트용 도서 정보를 등록합니다.
        BookInfo bookInfo = BookInfo.builder()
                .isbn("1234567890123")
                .title("테스트용 책 제목")
                .author("테스트용 책 저자")
                .publisher("테스트용 출판사")
                .image("http://example.com/test.jpg")
                .publishedAt("2023")
                .category(category)
                .build();
        bookInfoRepository.save(bookInfo);

        // 테스트용 도서 정보를 등록합니다.
        BookInfo bookInfo1 = BookInfo.builder()
                .isbn("1234567890121")
                .title("테스트용 책 제목")
                .author("테스트용 책 저자")
                .publisher("테스트용 출판사")
                .image("http://example.com/test.jpg")
                .publishedAt("2023")
                .category(category)
                .build();
        bookInfoRepository.save(bookInfo1);

        book = bookRepository.save(Book.builder()
                .callSign("111.111-11-11.c1")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());

        Book book2 = bookRepository.save(Book.builder()
                .callSign("111.111-11-11.c2")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());
    }

    @Nested
    @DisplayName("도서 등록")
    public class CreateBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 등록 성공")
            public void createBookSuccess() throws Exception {
                // Given
                String requestUrl = "/api/admin/book";
                String callSign = "111.111-11-11.c100";
                String isbn = "1234567890113";
                String title = "테스트용 책 제목";
                String author = "테스트용 책 저자";
                String publisher = "테스트용 출판사";
                String image = "http://example.com/test.jpg";
                String publishedAt = "2023";
                String categoryName = "테스트용";
                String status = "FINE";
                String donator = "테스트 기증자";

                String content = String.format("{\"callSign\":\"%s\", \"isbn\":\"%s\", \"title\":\"%s\", \"author\":\"%s\", \"publisher\":\"%s\", \"image\":\"%s\", \"publishedAt\":\"%s\", \"categoryName\":\"%s\", \"status\":\"%s\", \"donator\":\"%s\"}",
                        callSign, isbn, title, author, publisher, image, publishedAt, categoryName, status, donator);

                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FaileTest {
            @Test
            @DisplayName("청구기호가 중복되는 경우 예외 발생")
            public void createBookFailBecauseAlreadyExistCallsign() throws Exception {
                // Given
                String requestUrl = "/api/admin/book";
                String callSign = "111.111-11-11.c1";
                String isbn = "1234567890123";
                String title = "테스트용 책 제목";
                String author = "테스트용 책 저자";
                String publisher = "테스트용 출판사";
                String image = "http://example.com/test.jpg";
                String publishedAt = "2023";
                String categoryName = "테스트용";
                String status = "FINE";
                String donator = "테스트 기증자";

                String content = String.format("{\"callSign\":\"%s\", \"isbn\":\"%s\", " +
                                "\"title\":\"%s\", \"author\":\"%s\", \"publisher\":\"%s\", \"image\":\"%s\", \"publishedAt\":\"%s\", \"categoryName\":\"%s\", \"status\":\"%s\", \"donator\":\"%s\"}",
                        callSign, isbn, title, author, publisher, image, publishedAt, categoryName, status, donator);

                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.DUPLICATE_BOOK.getMessage()));
            }

            @Test
            @DisplayName("요청되는 변수들이 올바르지 않은 경우 예외 발생")
            public void requestedVariablesIncorrect() throws Exception {
                // Given
                String requestUrl = "/api/admin/book";
                String callSign = "";
                String isbn = "";
                String title = "";
                String author = "";
                String publisher = "";
                String image = "";
                String publishedAt = "";
                String categoryName = "";
                String status = "";
                String donator = "";

                String content = String.format("{\"callSign\":\"%s\", \"isbn\":\"%s\", " +
                                "\"title\":\"%s\", \"author\":\"%s\", \"publisher\":\"%s\", \"image\":\"%s\", \"publishedAt\":\"%s\", \"categoryName\":\"%s\", \"status\":\"%s\", \"donator\":\"%s\"}",
                        callSign, isbn, title, author, publisher, image, publishedAt, categoryName, status, donator);

                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(CommonErrorCode.INVALID_PARAMETER.getMessage()))
                        .andExpect(jsonPath("$.errors.size()").value(10));
            }
        }
    }

    @Nested
    @DisplayName("도서 조회")
    public class GetBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("청구기호로 도서 조회")
            public void getBookSuccess() throws Exception {
                // Given
                String requestUrl = "/api/admin/book/callsign";
                String callSign = "111.111-11-11.c1";
                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(get(requestUrl)
                                .param("callsign", callSign)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.callSign").value(callSign));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FaileTest {
            @Test
            @DisplayName("도서 조회시 청구기호가 존재하지 않으면 예외 발생")
            public void testGetBookFailBecauseNotExistBook() throws Exception {
                // Given
                String requestUrl = "/api/admin/book/callsign";
                String nonExistentCallSign = "111.111-11-11.c100";
                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(get(requestUrl)
                        .param("callsign", nonExistentCallSign)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.BAD_REQUEST_BOOK.getMessage()));
            }

        }
    }

    @Nested
    @DisplayName("도서 수정")
    public class UpdateBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 수정 성공")
            public void testUpdateBookSuccess() throws Exception {
                // Given
                String rquestUrl = "/api/admin/book";

                // bookId는 @BeforeEach에서 생성
                String callSign = "111.111-11-11.c100";
                String isbn = "1234567890123";
                String title = "테스트용 책 제목";
                String author = "테스트용 책 저자";
                String publisher = "테스트용 출판사";
                String image = "http://example.com/test.jpg";
                String publishedAt = "2023";
                String categoryName = "테스트용";
                String status = "FINE";
                String donator = "테스트 기증자";

                String content = String.format("{\"id\":\"%s\", \"callSign\":\"%s\", \"isbn\":\"%s\", \"title\":\"%s\", \"author\":\"%s\", \"publisher\":\"%s\", \"image\":\"%s\", \"publishedAt\":\"%s\", \"categoryName\":\"%s\", \"status\":\"%s\", \"donator\":\"%s\"}",
                        book.getId(), callSign, isbn, title, author, publisher, image, publishedAt,
                        categoryName, status, donator);

                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(put(rquestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FaileTest {
            @Test
            @DisplayName("도서 수정시 이미 존재하는 청구기호로 수정하면 예외 발생")
            public void testUpdateBookFailBecauseDuplicateCallSign() throws Exception {
                // Given
                String rquestUrl = "/api/admin/book";

                // bookId는 @BeforeEach에서 생성
                String callSign = "111.111-11-11.c2"; // 이미 존재하는 청구기호
                String isbn = "1234567890123";
                String title = "테스트용 책 제목";
                String author = "테스트용 책 저자";
                String publisher = "테스트용 출판사";
                String image = "http://example.com/test.jpg";
                String publishedAt = "2023";
                String categoryName = "테스트";
                String status = "FINE";
                String donator = "테스트 기증자";

                String authorizationHeader = "Bearer " + token;

                String content = String.format("{\"id\":\"%s\", \"callSign\":\"%s\", \"isbn\":\"%s\", \"title\":\"%s\", \"author\":\"%s\", \"publisher\":\"%s\", \"image\":\"%s\", \"publishedAt\":\"%s\", \"categoryName\":\"%s\", \"status\":\"%s\", \"donator\":\"%s\"}",
                        bookId, callSign, isbn, title, author, publisher, image, publishedAt, categoryName, status, donator);

                // When
                ResultActions resultActions = mockMvc.perform(put(rquestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.DUPLICATE_CALLSIGN.getMessage()));
            }

            @Test
            @DisplayName("도서 수정시 이미 존재하는 isbn로 수정하면 예외 발생")
            public void testUpdateBookFailBecauseDuplicateISBN() throws Exception {
                // Given
                String rquestUrl = "/api/admin/book";

                // bookId는 @BeforeEach에서 생성
                String callSign = "111.111-11-11.c1"; // 이미 존재하는 청구기호
                String isbn = "1234567890121";
                String title = "테스트용 책 제목";
                String author = "테스트용 책 저자";
                String publisher = "테스트용 출판사";
                String image = "http://example.com/test.jpg";
                String publishedAt = "2023";
                String categoryName = "테스트";
                String status = "FINE";
                String donator = "테스트 기증자";

                String authorizationHeader = "Bearer " + token;

                String content = String.format("{\"id\":\"%s\", \"callSign\":\"%s\", \"isbn\":\"%s\", \"title\":\"%s\", \"author\":\"%s\", \"publisher\":\"%s\", \"image\":\"%s\", \"publishedAt\":\"%s\", \"categoryName\":\"%s\", \"status\":\"%s\", \"donator\":\"%s\"}",
                        book.getId(), callSign, isbn, title, author, publisher, image, publishedAt,
                        categoryName, status, donator);

                // When
                ResultActions resultActions = mockMvc.perform(put(rquestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.DUPLICATE_ISBN.getMessage()));
            }
        }
    }

    @Nested
    @DisplayName("도서 삭제")
    public class DeleteBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 삭제 성공")
            public void testDeleteBookSuccess() throws Exception {
                // Given
                String requestUrl = "/api/admin/book/{bookId}";
                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(delete(requestUrl, book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FaileTest {
            @Test
            @DisplayName("도서 삭제시 청구기호가 존재하지 않으면 예외 발생 ")
            public void testDeleteBookFailBecauseNotExistBook() throws Exception {
                // Given
                String requestUrl = "/api/admin/book/{bookId}";
                Long bookId = 10000L; // 존재하지 않는 도서의 ID를 적절히 설정해주세요.
                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(delete(requestUrl, bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.BAD_REQUEST_BOOK.getMessage()));
            }

            @Test
            @DisplayName("도서 삭제시 대여중인 도서일 시 예외발생")
            public void testDeleteBookFailBecauseExistRentBook() throws Exception {
                // Given
                String requestUrl = "/api/admin/book/{bookId}";
                String authorizationHeader = "Bearer " + token;

                book.updateLendingStatus();
                bookRepository.save(book);

                // When
                ResultActions resultActions = mockMvc.perform(delete(requestUrl, book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.NOT_DELETE_BECAUSE_RENT_BOOK.getMessage()));
            }
        }
    }
}
