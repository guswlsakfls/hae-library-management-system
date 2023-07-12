package com.hae.library.controllerTest;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("BookController 통합 테스트")
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String token;

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
        mockMvc.perform(post("/api/admin/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\":\"테스트\"}")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 테스트용 도서 정보를 등록합니다.
        mockMvc.perform(post("/api/admin/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"callSign\":\"111.111-11-11.c1\", \"isbn\":\"1234567890123\"," +
                                " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", \"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
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
                                    .content("{\"callSign\":\"111.111-11-11.c2\", \"isbn\":\"1234567890123\"," +
                                            " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", \"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}")
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
                                        " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", \"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}")
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
                mockMvc.perform(get("/api/admin/book/{callsign}", "111.111-11-11.c1")
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
                mockMvc.perform(get("/api/admin/book/{callsign}", "111.111-11-11.c2")
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
                mockMvc.perform(put("/api/admin/book/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":\"1\", \"callSign\":\"111.111-11-11.c1000\", " +
                                        "\"isbn\":\"1234567890123\"," +
                                        " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", \"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}")
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
                mockMvc.perform(put("/api/admin/book/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\":\"1\", \"callSign\":\"111.111-11-11.c1\", " +
                                        "\"isbn\":\"1234567890123\"," +
                                        " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", \"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}")
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
                mockMvc.perform(delete("/api/admin/book/{bookId}/delete", 5L)
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
