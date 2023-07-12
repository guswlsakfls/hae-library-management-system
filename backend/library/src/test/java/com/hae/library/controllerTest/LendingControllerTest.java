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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Lending 통합 테스트")
public class LendingControllerTest {
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
                        .content("{\"callSign\":\"111.111-11-11.c1\", \"isbn\":\"1234567891111\"," +
                                " \"title\":\"테스트용 책 제목\", \"author\":\"테스트용 책 저자\", \"publisher\":\"테스트용 출판사\", \"image\":\"http://example.com/test.jpg\", \"publishedAt\":\"2023\", \"categoryName\":\"테스트\", \"status\":\"FINE\", \"donator\":\"테스트 기증자\"}")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Nested
    @DisplayName("대출 정보 생성")
    class CreateLending {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("대출 정보를 생성합니다.")
            public void createLending() throws Exception {
                // TODO: 테스트 도서를 어떻게 선정할지(실제 db에 있는 도서를 가져오는 것이 좋을지) 고민해보기
                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"userId\":1, \"bookId\":1, " +
                                        "\"lendingCondition\":\"테스트트\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("도서가 존재하지 않을 때")
            public void createLendingWhenBookDoesNotExist() throws Exception {
                // TODO: bookId를 실제로 존재하지 않는 값으로 설정
                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"userId\":1, \"bookId\":10000, " +
                                        "\"lendingCondition\":\"테스트\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }

            @Test
            @DisplayName("도서가 대출 중일 때")
            public void createLendingWhenBookIsLending() throws Exception {

            }

            @Test
            @DisplayName("도서가 분실 상태일 때")
            public void createLendingWhenBookIsLost() throws Exception {

            }

            @Test
            @DisplayName("회원이 정지 상태일 때")
            public void createLendingWhenUserIsSuspended() throws Exception {

            }

            @Test
            @DisplayName("회원이 대출 가능한 권수를 초과했을 때")
            public void createLendingWhenUserExceedsLendingLimit() throws Exception {

            }
        }
    }

    @Nested
    @DisplayName("대출된 도서 반납 처리")
    class ReturnLending {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("대출된 도서를 반납 처리합니다.")
            public void returnLending() throws Exception {
                mockMvc.perform(put("/api/admin/lending/returning")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"userId\":1, \"bookId\":1}") // TODO: 도서를 대출하고 id 가지고
                                // 와야 한다.
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("반납할 도서가 대출 중이 아닐 때")
            public void returnLendingWhenBookDoesNotExist() throws Exception {
            }
        }
    }

    @Nested
    @DisplayName("대출 목록 조회")
    class GetCategoryList {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("대출 전체 목록을 조회합니다.")
            public void getCategoryList() throws Exception {
            }
        }
    }

    @Nested
    @DisplayName("대출 정보 삭제")
    class updateCategory {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
//            @Test
//            @DisplayName("카테고리를 수정합니다.")
        }
    }
}
