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
@DisplayName("Category 통합 테스트")
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
                        .content("{\"categoryName\":\"테스트용입니다\"}")
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Nested
    @DisplayName("카테고리 생성")
    class CreateCategory {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("카테고리를 생성합니다.")
            public void createCategory() throws Exception {
                mockMvc.perform(post("/api/admin/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"categoryName\":\"테스트\"}")
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
            @DisplayName("카테고리 이름이 null일 경우")
            public void createCategoryWithNullCategoryName() throws Exception {
                mockMvc.perform(post("/api/admin/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"categoryName\":null}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }

            @Test
            @DisplayName("카테고리 이름이 빈 문자열일 경우")
            public void createCategoryWithEmptyCategoryName() throws Exception {
                mockMvc.perform(post("/api/admin/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"categoryName\":\"\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }

            @Test
            @DisplayName("카테고리 이름이 20자를 초과할 경우")
            public void createCategoryWithLongCategoryName() throws Exception {
                mockMvc.perform(post("/api/admin/category/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"categoryName\":\"123456789012345678901\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }
        }
    }

    @Nested
    @DisplayName("카테고리 목록 조회")
    class GetCategoryList {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("카테고리 목록을 조회합니다.")
            public void getCategoryList() throws Exception {
                mockMvc.perform(get("/api/category/all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }
        }
    }

    @Nested
    @DisplayName("카테고리 수정")
    class updateCategory {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("카테고리를 수정합니다.")
            public void updateCategory() throws Exception {
                mockMvc.perform(put("/api/admin/category/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"categoryId\":1, \"updatedCategoryName\":\"테스트\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }
        }
    }
}
