package com.hae.library.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @DisplayName("GET /api/bookInfo/all")
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
        }
    }

    @Nested
    @DisplayName("GET /api/bookInfo/{bookId}")
    public class GetBookInfo {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 정보를 조회합니다.")
            public void getBookInfo() throws Exception {
                mockMvc.perform(get("/api/bookinfo/1")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.data.id", is(1)));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 도서 정보를 조회합니다.")
            public void getBookInfo() throws Exception {
                mockMvc.perform(get("/api/bookinfo/1000")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message", is("존재하지 않는 책 정보입니다.")));
            }
        }
    }

    @Nested
    @DisplayName("POST /api/admin/bookInfo/isbn")
    public class GetBookInfoByIsbn {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
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
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
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
    @DisplayName("DELETE /api/admin/bookinfo/{bookInfoId}/delete")
    public class DeleteBookInfo {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 정보를 삭제합니다.")
            public void deleteBookInfo() throws Exception {
                mockMvc.perform(delete("/api/admin/bookinfo/1/delete")
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

