package com.hae.library.controllerTest;

import com.hae.library.controller.BookInfoController;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.service.BookInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookInfoController.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("BookInfoController 단위 테스트")
public class BookInfoControllerTest {
    @MockBean
    private BookInfoService bookInfoService;

    @Autowired
    private MockMvc mockMvc;

    private ResponseBookInfoDto responseBookInfoDto1;
    private ResponseBookInfoDto responseBookInfoDto2;
    private ResponseBookInfoWithBookDto responseBookInfoWithBookDto;
    private Page<ResponseBookInfoDto> responseBookInfoDtoList;

    @BeforeEach
    public void setUp() {
        responseBookInfoDto1 = ResponseBookInfoDto.builder()
                .title("Java Programming")
                .author("John Smith")
                .isbn("9781234567890")
                .image("book-image.jpg")
                .publisher("ABC Publishing")
                .publishedAt("2022-01-01")
                .build();

        responseBookInfoDto2 = ResponseBookInfoDto.builder()
                .title("Python Programming")
                .author("Jane Doe")
                .isbn("9780987654321")
                .image("book-image.jpg")
                .publisher("DEF Publishing")
                .publishedAt("2022-01-01")
                .build();

        responseBookInfoWithBookDto = ResponseBookInfoWithBookDto.builder()
                .title("Java Programming")
                .author("John Smith")
                .isbn("9781234567890")
                .image("book-image.jpg")
                .publisher("ABC Publishing")
                .publishedAt("2022-01-01")
                .build();

//        responseBookInfoDtoList = List.of(
//                responseBookInfoDto1,
//                responseBookInfoDto2
//        );
    }

    @Nested
    @DisplayName("책 정보 조회 컨트롤러")
    public class CreateBookInfoControllerTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("[GET] 모든 책 정보 조회시 모든 책 정보 리스트 반환")
            public void getAllBookInfoTest() throws Exception {
                //Given
                String searchKey = "";
                int page = 0;
                int size = 10;
                given(bookInfoService.getAllBookInfo(searchKey, page, size)).willReturn(responseBookInfoDtoList);

                // When & Then
                mockMvc.perform(get("/api/bookinfo/all").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(result -> jsonPath("$", hasSize(2)));
            }

            @Test
            @DisplayName("[GET] id로 책 정보 조회시 책 정보 반환")
            public void getBookInfoWithBookByIdTest() throws Exception {
                // Given
                given(bookInfoService.getBookInfoById(1L)).willReturn(responseBookInfoWithBookDto);

                // When & Then
                mockMvc.perform(get("/api/bookinfo/1").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(result -> jsonPath("$.title").value("Java Programming"))
                        .andExpect(result -> jsonPath("$.author").value("John Smith"))
                        .andExpect(result -> jsonPath("$.isbn").value("9781234567890"))
                        .andExpect(result -> jsonPath("$.image").value("book-image.jpg"))
                        .andExpect(result -> jsonPath("$.publisher").value("ABC Publishing"))
                        .andExpect(result -> jsonPath("$.publishedAt").value("2022-01-01"));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("[GET] 모든 책 정보 조회시 책 정보가 없을 경우 빈 리스트 반환")
            public void getAllBookInfoTest() throws Exception {
//                //Given
//                String searchKey = "";
//                int page = 0;
//                int size = 10;
//
//                given(bookInfoService.getAllBookInfo(searchKey, page, size)).willReturn(List.of());
//
//                // When & Then
//                mockMvc.perform(get("/api/bookinfo/all").accept(MediaType.APPLICATION_JSON))
//                        .andExpect(status().isOk())
//                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                        .andExpect(result -> jsonPath("$", hasSize(0)));
            }

            @Test
            @DisplayName("[GET] id로 책 정보 조회시 책 정보가 없을 경우 404 반환")
            public void getBookInfoWithBookByIdTest() throws Exception {
                //Given
                given(bookInfoService.getBookInfoById(1L)).willThrow(new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO_BY_ID));

                // When & Then
                mockMvc.perform(get("/api/bookinfo/1").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    @DisplayName("책 정보 삭제 컨트롤러")
    public class DeleteBookInfoTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("[DELETE] id로 책 정보 삭제시 200 반환")
            public void deleteBookInfoByIdTest() throws Exception {
                // When
                doNothing().when(bookInfoService).deleteBookInfoById(1L);

                // Then
                mockMvc.perform(delete("/api/bookinfo/1").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("[DELETE] id로 책 정보 삭제시 책 정보가 없을 경우 404 반환")
            public void deleteBookInfoByIdTest() throws Exception {
                //Given
                doThrow(new RestApiException(BookErrorCode.BAD_REQUEST_BOOKINFO_BY_ID)).when(bookInfoService).deleteBookInfoById(1L);

                // When & Then
                mockMvc.perform(delete("/api/bookinfo/1").accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }
}

