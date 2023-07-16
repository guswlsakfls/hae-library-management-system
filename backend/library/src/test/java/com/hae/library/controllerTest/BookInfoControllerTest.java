package com.hae.library.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.domain.Enum.BookStatus;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("BookInfoController 통합 테스트")
public class BookInfoControllerTest {
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
        BookInfo book1 = bookInfoRepository.save(bookInfo);

        Book book2 = bookRepository.save(Book.builder()
                .callSign("111.111-11-11.c1")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());

        Book book3 = bookRepository.save(Book.builder()
                .callSign("111.111-11-11.c2")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());

        this.bookId = book1.getId();
    }

    @Nested
    @DisplayName("도서 정보 조회")
    public class GetAllBookInfo {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("모든 도서 정보를 조회합니다.")
            public void getAllBookInfo() throws Exception {
                mockMvc.perform(get("/api/bookinfo/all")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "최신도서")
                                .param("role", "전체")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            }

            @Test
            @DisplayName("도서 정보를 id로 조회합니다.")
            public void getBookInfoById() throws Exception {
                mockMvc.perform(get("/api/bookinfo/" + bookId)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
            }
        }

        @Test
        @DisplayName("ISBN으로 도서 정보를 조회합니다.")
        public void getBookInfoByIsbn() throws Exception {
            mockMvc.perform(get("/api/admin/bookinfo/isbn")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("isbn", "1234567890123")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.data.title", is("테스트용 책 제목")));
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("id가 존재하지 않는 도서 정보를 조회합니다.")
            public void notGetBookInfoById() throws Exception {
                mockMvc.perform(get("/api/bookinfo/1000")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message", is("존재하지 않는 책 정보입니다.")));
            }

            @Test
            @DisplayName("존재하지 않는 ISBN으로 도서 정보를 조회합니다.")
            public void getBookInfoByIsbn() throws Exception {
                mockMvc.perform(get("/api/admin/bookinfo/isbn")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("isbn", "1221507890123")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message", is("존재하지 않는 책 정보입니다.")));
            }
        }
    }

    @Nested
    @DisplayName("도서 정보를 삭제")
    public class DeleteBookInfo {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("id로 도서 정보를 삭제합니다.")
            public void deleteBookInfoById() throws Exception {
                mockMvc.perform(delete("/api/admin/bookinfo/" + bookId + "/delete")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message", is("책 정보 삭제에 성공하였습니다")));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 도서 정보를 삭제합니다.")
            public void deleteBookInfo() throws Exception {
                mockMvc.perform(delete("/api/admin/bookinfo/1000/delete")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message", is("존재하지 않는 책입니다.")));
            }
        }
    }
}

