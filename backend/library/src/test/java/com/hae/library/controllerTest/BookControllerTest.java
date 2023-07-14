package com.hae.library.controllerTest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    public void setup() throws Exception {
        // 로그인하여 토큰을 발급받습니다.
        String loginResponse = mockMvc.perform(post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@gmail.com\", \"password\":\"ehdaud11!\"}"))
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

//        bookInfo.addCategory(category);
        bookInfoRepository.save(bookInfo);

        Book book1 = bookRepository.save(Book.builder()
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

        this.bookId = book1.getId();
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
                    mockMvc.perform(post("/api/admin/book/create")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("{\"callSign\":\"111.111-11-11.c100\", " +
                                            "\"isbn\":\"1234567890113\"," +
                                            " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", " +
                                            "\"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트용\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}")
                                    .header("Authorization", "Bearer " + token))
                            .andExpect(status().isOk());
                }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FaileTest {
            @Test
            @DisplayName("청구기호가 중복되는 경우 예외 발생")
            public void createBookFailBecauseAlreadyExistBook() throws Exception {
                mockMvc.perform(post("/api/admin/book/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"callSign\":\"111.111-11-11.c1\", \"isbn\":\"1234567890123\"," +
                                        " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", " +
                                        "\"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트용\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.DUPLICATE_BOOK.getMessage()));
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
                mockMvc.perform(get("/api/admin/book/callsign")
                                .param("callsign", "111.111-11-11.c1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.callSign").value("111.111-11-11.c1"));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FaileTest {
            @Test
            @DisplayName("도서 조회시 청구기호가 존재하지 않으면 예외 발생 ")
            public void getBookFailBecauseNotExistBook() throws Exception {
                mockMvc.perform(get("/api/admin/book/callsign")
                                .param("callsign", "111.111-11-11.c100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
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
            public void updateBookSuccess() throws Exception {
                String content = String.format("{\"id\":\"%s\", \"callSign\":\"111.111-11-11.c100\", " +
                        "\"isbn\":\"1234567890123\"," +
                        " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", \"publisher\":\"테스트용" +
                        " 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트용\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}", bookId);

                mockMvc.perform(put("/api/admin/book/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FaileTest {
            @Test
            @DisplayName("도서 수정시 이미 존재하는 청구기호로 수정하면 예외 발생")
            public void updateBookFailBecauseNotExistBook() throws Exception {
                String content = String.format("{\"id\":\"%s\", \"callSign\":\"111.111-11-11.c2\", " +
                        "\"isbn\":\"1234567890123\"," +
                        " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", \"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}", bookId);

                mockMvc.perform(put("/api/admin/book/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.DUPLICATE_CALLSIGN.getMessage()));
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
            public void deleteBookSuccess() throws Exception {
                mockMvc.perform(delete("/api/admin/book/{bookId}/delete", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FaileTest {
            @Test
            @DisplayName("도서 삭제시 청구기호가 존재하지 않으면 예외 발생 ")
            public void deleteBookFailBecauseNotExistBook() throws Exception {
                mockMvc.perform(delete("/api/admin/book/{bookId}/delete", 10000L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value(BookErrorCode.BAD_REQUEST_BOOK.getMessage()));
            }
        }
    }
}
