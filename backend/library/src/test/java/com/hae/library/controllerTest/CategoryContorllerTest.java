package com.hae.library.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.Category;
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
@DisplayName("Category 통합 테스트")
public class CategoryContorllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    private String token;

    private Long categoryId;

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
                .categoryName("테스트용입니다")
                .build());
        this.categoryId = category.getId();

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
                // Given
                String requestUrl = "/api/admin/category/create";
                String categoryName = "테스트";
                String content = String.format("{\"categoryName\":\"%s\"}", categoryName);
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
        public class Fail {
            @Test
            @DisplayName("카테고리 이름이 빈 값일 경우")
            public void createCategoryWithNullCategoryName() throws Exception {
                // Given
                String requsetUrl = "/api/admin/category/create";
                String categoryName = "";
                String requestJson = String.format("{\"categoryName\":\"%s\"}", categoryName);

                // When
                ResultActions resultActions = mockMvc.perform(post(requsetUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isBadRequest());
            }


            @Test
            @DisplayName("카테고리 이름이 빈 문자열일 경우")
            public void createCategoryWithEmptyCategoryName() throws Exception {
                // Given
                String requestUrl = "/api/admin/category/create";
                String categoryName = "";
                String content = String.format("{\"categoryName\":\"%s\"}", categoryName);

                // When
                ResultActions resultActions = mockMvc.perform(post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("카테고리 이름이 20자를 초과할 경우")
            public void createCategoryWithLongCategoryName() throws Exception {
                // Given
                String requestUrl = "/api/admin/category/create";
                String categoryName = "123456789012345678901";
                String requestJson = String.format("{\"categoryName\":\"%s\"}", categoryName);

                // When
                ResultActions resultActions = mockMvc.perform(post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isBadRequest());
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
                // Given
                String requestUrl = "/api/category/all";

                // When
                ResultActions resultActions = mockMvc.perform(get(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isOk())
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
                // Given
                String requestUrl = "/api/admin/category/update";
                String updatedCategoryName = "테스트";
                String content = String.format("{\"categoryId\":%d, \"updatedCategoryName\":\"%s\"}", categoryId, updatedCategoryName);

                // When
                ResultActions resultActions = mockMvc.perform(put(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("id에 해당하는 카테고리가 존재하지 않을 경우")
            public void notExistCategoryById() throws Exception {
                // Given
                String requestUrl = "/api/admin/category/update";
                long nonExistCategoryId = 999;
                String updatedCategoryName = "테스트";
                String content = String.format("{\"categoryId\":%d, " +
                        "\"updatedCategoryName\":\"%s\"}", nonExistCategoryId, updatedCategoryName);

                // When
                ResultActions resultActions = mockMvc.perform(put(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }

        }
    }

    @Nested
    @DisplayName("카테고리 삭제")
    public class DeleteCategory {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("카테고리를 삭제합니다.")
            public void deleteCategory() throws Exception {
                // Given
                String requestUrl = "/api/admin/category/" + categoryId + "/delete";

                // When
                ResultActions resultActions = mockMvc.perform(delete(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }
        }


        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("id에 해당하는 카테고리가 존재하지 않을 경우")
            public void notExistCategoryById() throws Exception {
                // Given
                String nonExistCategoryId = "10000";
                String requestUrl = "/api/admin/category/" + nonExistCategoryId + "/delete";

                // When
                ResultActions resultActions = mockMvc.perform(delete(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }

        }
    }
}
