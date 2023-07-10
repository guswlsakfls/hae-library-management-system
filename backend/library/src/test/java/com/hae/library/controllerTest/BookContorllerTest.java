package com.hae.library.controllerTest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.controller.BookController;
import com.hae.library.controller.BookInfoController;
import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.Book.ResponseBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.Member.RequestLoginDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.mockCustomUser.WithMockCustomUser;
import com.hae.library.service.BookInfoService;
import com.hae.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
@ExtendWith(MockitoExtension.class)
@DisplayName("BookController 단위 테스트")
public class BookContorllerTest {
    @MockBean
    private BookService bookService;

    @MockBean
    private BookInfoService bookInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private String jwtToken;

    private RequestBookWithBookInfoDto requestBookWithBookInfoDto;
    private ResponseBookInfoDto responseBookInfoDto;
    private ResponseBookWithBookInfoDto responseBookWithBookInfoDto1;
    private ResponseBookWithBookInfoDto responseBookWithBookInfoDto2;
    private List<ResponseBookWithBookInfoDto> responseBookWithBookInfoDtoList;

    @BeforeEach
    public void setUp() throws Exception {
//        // 로그인 요청을 위한 DTO 생성
//        RequestLoginDto loginDto = new RequestLoginDto();
//        loginDto.setEmail("admin@gmail.com");
//        loginDto.setPassword("1234");
//
//        // 로그인 요청
//        MvcResult result = mockMvc.perform(post("/api/auth")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(loginDto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // JWT 토큰 추출
//        String jwtToken = result.getResponse().getHeader("Authorization");
//        System.out.println("jwtToken = " + jwtToken);

//        // 로그인 요청을 위한 DTO 생성
//        RequestLoginDto loginDto = new RequestLoginDto();
//        loginDto.setEmail("admin@gmail.com");
//        loginDto.setPassword("password");
//
//        // 로그인 API 호출
//        MvcResult result = mockMvc.perform(post("/api/auth")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginDto)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // JWT 토큰 추출
//        String response = result.getResponse().getContentAsString();
//        JsonNode responseJson = objectMapper.readTree(response);
//        jwtToken = responseJson.get("accessToken").asText();

        requestBookWithBookInfoDto = RequestBookWithBookInfoDto.builder()
                .id(1L)
                .callSign("123.12.v1.c1")
                .status("FINE")
                .title("Java Programming")
                .author("John Smith")
                .isbn("9781234567890")
                .image("book-image.jpg")
                .publisher("ABC Publishing")
                .publishedAt("2022-01-01")
                .build();

        responseBookInfoDto = ResponseBookInfoDto.builder()
                .title("Java Programming")
                .author("John Smith")
                .isbn("9781234567890")
                .image("book-image.jpg")
                .publisher("ABC Publishing")
                .publishedAt("2022-01-01")
                .build();

        responseBookWithBookInfoDto1 = ResponseBookWithBookInfoDto.builder()
                .id(1L)
                .bookInfo(responseBookInfoDto)
                .status(BookStatus.valueOf("FINE"))
                .callSign("123.12.v1.c1")
                .build();


        responseBookWithBookInfoDto2 = ResponseBookWithBookInfoDto.builder()
                .id(2L)
                .bookInfo(responseBookInfoDto)
                .status(BookStatus.valueOf("FINE"))
                .callSign("123.12.v1.c2")
                .build();

        responseBookWithBookInfoDtoList = List.of(responseBookWithBookInfoDto1, responseBookWithBookInfoDto2);
    }

    @Nested
    @DisplayName("[POST] 책 생성 컨트롤러")
    public class CreateBookControllerTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @WithMockCustomUser
            @DisplayName("책 데이터 입력시 책 생성")
            public void createBookWithBookInfoTest() throws Exception {
                // Given
                when(bookService.createBook(any(RequestBookWithBookInfoDto.class))).thenReturn(responseBookWithBookInfoDto1);

                // When & Then
                mockMvc.perform(post("/api/admin/book/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBookWithBookInfoDto))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.data").exists())
                        .andExpect(jsonPath("$.statusCode").value(200))
                        .andExpect(jsonPath("$.message").value("책이 성공적으로 등록되었습니다"));
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("책 데이터 NOTBLACNK 검증")
            public void validNotBlacnk() throws Exception {
                // Given
                RequestBookWithBookInfoDto dto = RequestBookWithBookInfoDto.builder()
                        .callSign("")
                        .isbn("")
                        .title("")
                        .author("")
                        .publisher("")
                        .image("")
                        .publishedAt("")
                        .status("")
                        .build();

                // When & Then
                mockMvc.perform(post("/api/book/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.errors.length()").value(8));
            }

            @Test
            @DisplayName("callSign이 없을 경우 에러 메시지 반환")
            public void validExceptionCallSignTest() throws Exception {
                // Given
                RequestBookWithBookInfoDto dto = RequestBookWithBookInfoDto.builder()
                        .callSign("")
                        .isbn("1234567890123")
                        .title("title")
                        .author("author")
                        .publisher("publisher")
                        .publishedAt("2023-06-26")
                        .status("status")
                        .image("image")
                        .build();

                // When & Then
                mockMvc.perform(post("/api/book/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message").value("옳지 않은 변수 요청입니다."))
                        .andExpect(jsonPath("$.errors[0].field").value("callSign"))
                        .andExpect(jsonPath("$.errors[0].message").value("도서 번호를 입력해주세요."));
            }

            @Test
            @DisplayName("isbn이 없을 경우 에러 메시지 반환")
            public void createBookWithBookInfoFailTest() throws Exception {
                // Given
                RequestBookWithBookInfoDto dto = RequestBookWithBookInfoDto.builder()
                        .callSign("123.12.v1.c1")
                        .isbn("")
                        .title("title")
                        .author("author")
                        .publisher("publisher")
                        .publishedAt("2023-06-26")
                        .status("status")
                        .image("image")
                        .build();

                // When & Then
                mockMvc.perform(post("/api/book/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message").value("옳지 않은 변수 요청입니다."))
                        .andExpect(jsonPath("$.errors[0].field").value("isbn"))
                        .andExpect(jsonPath("$.errors[0].message").value("ISBN을 입력해주세요."));
            }
        }
    }

    @Nested
    @DisplayName("[GET] 책 조회 컨트롤러")
    public class GetBookControllerTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {

            // TODO: 도서 관리 페이지 수정으로 필요 없어짐. (추후 쓰일 곳 있는지 보고 삭제)
//            @Test
//            @DisplayName("책 전체 조회")
//            public void getBooksTest() throws Exception {
//                // Given
//                String searchKey = "";
//                int page = 0;
//                int size = 10;
//                String categoryName = "전체";
//                String sort = "최신도서";
//
//                when(bookService.getAllBook(searchKey, page, size, categoryName, sort)).thenReturn(Page<responseBookWithBookInfoDtoList>);
//
//                // When & Then
//                mockMvc.perform(get("/api/book/all")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON))
//                        .andExpect(status().isOk())
//                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                        .andExpect(jsonPath("$.data.length()").value(2))
//                        .andExpect(jsonPath("$.statusCode").value(200))
//                        .andExpect(jsonPath("$.message").value("모든 책 조회에 성공하였습니다"));
//            }

            @Test
            @DisplayName("책 id로 조회")
            public void getBookTest() throws Exception {
                // Given
                when(bookService.getBookById(1L)).thenReturn(responseBookWithBookInfoDto1);

                // When & Then
                mockMvc.perform(get("/api/book/{id}/info", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.data.id").value(1L))
                        .andExpect(jsonPath("$.statusCode").value(200))
                        .andExpect(jsonPath("$.message").value("하나의 책 조회에 성공하였습니다"));
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {

            // TODO: 도서 관리 페이지 수정으로 필요 없어짐.(추후 쓰일 곳 있는지 보고 삭제)
//            @Test
//            @DisplayName("책 전체 조회시 책이 없을 경우 빈 리스트 반환")
//            public void getBooksFailTest() throws Exception {
//                // Given
//                when(bookService.getAllBook()).thenReturn(Collections.emptyList());
//
//                // When & Then
//                mockMvc.perform(get("/api/book/all")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON))
//                        .andExpect(status().isOk())
//                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                        .andExpect(jsonPath("$.data.length()").value(0))
//                        .andExpect(jsonPath("$.statusCode").value(200))
//                        .andExpect(jsonPath("$.message").value("모든 책 조회에 성공하였습니다"));
//            }

            @Test
            @DisplayName("책 상세 조회시 책이 없을 경우 에러 메시지 반환")
            public void getBookByIdTest() throws Exception {
                // Given
                when(bookService.getBookById(1L)).thenThrow(new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));

                // When & Then
                mockMvc.perform(get("/api/book/{id}/info", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 책입니다."));
            }
        }
    }

    @Nested
    @DisplayName("[PUT] 책 수정 컨트롤러")
    public class updateBookController {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책 정보 수정시 수정된 책 정보 리턴")
            public void updateBookByIdTest() throws Exception {
                // Given
                when(bookService.updateBook(any(RequestBookWithBookInfoDto.class))).thenReturn(responseBookWithBookInfoDto1);

                // When & Then
                mockMvc.perform(put("/api/book/modify")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBookWithBookInfoDto))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.data").exists())
                        .andExpect(jsonPath("$.statusCode").value(200))
                        .andExpect(jsonPath("$.message").value("책 수정에 성공하였습니다"));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("책 정보 수정시 책이 없을 경우 에러 메시지 반환")
            public void updateBookByIdFailTest() throws Exception {
                // Given
                when(bookService.updateBook(any(RequestBookWithBookInfoDto.class))).thenThrow(new RestApiException(BookErrorCode.BAD_REQUEST_BOOK));

                // When & Then
                mockMvc.perform(put("/api/book/modify")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestBookWithBookInfoDto))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 책입니다."));
            }
        }
    }

    @Nested
    @DisplayName("[DELETE] 책 삭제 컨트롤러")
    public class DeleteBookControllerTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책 삭제시 삭제 성공 리턴")
            public void deleteBookByIdTest() throws Exception {
                // Given
                doNothing().when(bookService).deleteBookById(1L);

                // When & Then
                mockMvc.perform(delete("/api/book/{id}/delete", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.data").doesNotExist())
                        .andExpect(jsonPath("$.statusCode").value(200))
                        .andExpect(jsonPath("$.message").value("책 삭제에 성공하였습니다"));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("책 삭제시 책이 없을 경우 에러 메시지 반환")
            public void deleteBookByIdFailTest() throws Exception {
                // Given
                doThrow(new RestApiException(BookErrorCode.BAD_REQUEST_BOOK)).when(bookService).deleteBookById(1L);

                // When & Then
                mockMvc.perform(delete("/api/book/{id}/delete", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.message").value("존재하지 않는 책입니다."));
            }
        }
    }
}
