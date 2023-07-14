package com.hae.library.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.*;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.domain.Enum.Role;
import com.hae.library.repository.*;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Lending 통합 테스트")
public class RequestBookContorllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private RequestBookRepository requestBookRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private BookInfoRepository bookInfoRepo;

    private String token;
    private BookInfo bookInfo;
    private Member member;
    private Long categoryId;
    private Long bookInfoId;
    private Long bookId;

    @BeforeEach
    void setUp() throws Exception {
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
        Category category = categoryRepo.save(Category.builder()
                .categoryName("테스트")
                .build());
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
        bookInfoRepo.save(bookInfo);

        Book book1 = bookRepo.save(Book.builder()
                .callSign("111.111-11-11.c1")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());

        Book book2 = bookRepo.save(Book.builder()
                .callSign("111.111-11-11.c2")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());

        this.bookId = book1.getId();
    }

    @Nested
    @DisplayName("도서 구매 요청")
    class RequestBookTest {
        @Nested
        @DisplayName("성공 케이스")
        class Success {
            @Test
            @DisplayName("도서 구매 요청을 저장")
            void saveRequestBook() throws Exception {
                // when & then
                mockMvc.perform(post("/api/admin/request-book/create")
                                .contentType("application/json")
                        .content("{\"isbn\":\"1234567890113\"," +
                                " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", " +
                                "\"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\"}")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class Fail {
            @Test
            @DisplayName("중복되는 isbn으로 도서 구매 요청시 예외 발생")
            void saveRequestBook() throws Exception {
                // when & then
                mockMvc.perform(post("/api/admin/request-book/create")
                                .contentType("application/json")
                                .content("{\"isbn\":\"1234567890113\"," +
                                        " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", " +
                                        "\"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());

                mockMvc.perform(post("/api/admin/request-book/create")
                                .contentType("application/json")
                                .content("{\"isbn\":\"1234567890113\"," +
                                        " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", " +
                                        "\"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("도서 구매 요청 목록 조회")
    class GetRequestBookList {
        @Nested
        @DisplayName("성공 케이스")
        class Success {
            @Test
            @DisplayName("도서 구매 요청 목록을 조회")
            void getRequestBookList() throws Exception {
                // Given

                // When & Then
                mockMvc.perform(get("/api/admin/request-book/all")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "최신도서")
                                .param("role", "전체")
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("도서 구매 요청 삭제")
    class DeleteRequestBook {
        @Nested
        @DisplayName("성공 케이스")
        class Success {
            @Test
            @DisplayName("도서 구매 요청을 삭제")
            void deleteRequestBook() throws Exception {
                // Given
                RequestBook requestBook = requestBookRepo.save(RequestBook.builder()
                        .bookInfo(bookInfo)
                        .member(member)
                        .isApproved(false)
                        .build());

                // When & Then
                mockMvc.perform(delete("/api/admin/request-book/{requestBookId}/delete",
                                requestBook.getId())
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class Fail {
            @Test
            @DisplayName("존재하지 않는 도서 구매 요청 삭제시 예외 발생")
            void deleteRequestBook() throws Exception {
                // Given

                // When & Then
                mockMvc.perform(delete("/api/admin/request-book/{requestBookId}/delete", 1000L)
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
