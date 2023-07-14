package com.hae.library.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.*;
import com.hae.library.domain.Enum.BookStatus;
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
public class LendingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LendingRepository lendingRepository;

    private String token;

    private Long categoryId;

    private Long book1Id;
    private Long book2Id;
    private Long book3Id;
    private Long book4Id;

    private Long bookInfoId;

    Book book1;

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
        BookInfo bookInfo1 = bookInfoRepository.save(bookInfo);

        book1 = bookRepository.save(Book.builder()
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

        Book book3 = bookRepository.save(Book.builder()
                .callSign("111.111-11-11.c3")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());

        Book book4 = bookRepository.save(Book.builder()
                .callSign("111.111-11-11.c4")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());

        this.book1Id = book1.getId();
        this.book2Id = book2.getId();
        this.book3Id = book3.getId();
        this.book4Id = book4.getId();
        this.bookInfoId = bookInfo1.getId();
    }

    @Nested
    @DisplayName("대출 정보 생성")
    class CreateLending {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("대출 정보를 생성")
            public void createLending() throws Exception {
                String content = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book1Id);

                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
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
            @DisplayName("도서가 존재하지 않을 때 예외 발생")
            public void createLendingWhenBookDoesNotExist() throws Exception {
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
            @DisplayName("도서가 대출 중일 때 예외 발생")
            public void createLendingWhenBookIsLending() throws Exception {
                String content = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book1Id);

                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }

            @Test
            @DisplayName("도서가 분실 상태일 때 예외발생")
            public void createLendingWhenBookIsLost() throws Exception {
                book1.updateBookStatus(BookStatus.valueOf("LOST"));

                String content = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book1Id);

                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .header("Authorization", "Bearer " + token));
            }

            @Test
            @DisplayName("회원이 정지 상태일 때 예외발생")
            public void createLendingWhenUserIsSuspended() throws Exception {
                Member member = memberRepository.findById(1L).get();
                member.updateActivated(false);

                String content = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book1Id);
            }

            @Test
            @DisplayName("회원이 대출 가능한 권수를 초과했을 때 예외발생")
            public void createLendingWhenUserExceedsLendingLimit() throws Exception {
                String content1 = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book1Id);
                String content2 = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book2Id);
                String content3 = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book3Id);
                String content4 = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book4Id);

                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content1)
                                .header("Authorization", "Bearer " + token));

                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content2)
                                .header("Authorization", "Bearer " + token));

                mockMvc.perform(post("/api/admin/lending/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content3)
                        .header("Authorization", "Bearer " + token));

                mockMvc.perform(post("/api/admin/lending/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content4)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
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
                String content1 = String.format("{\"userId\":1, \"bookId\":%d, " +
                        "\"lendingCondition\":\"테스트트\"}", book1Id);

                // 대출 처리
                mockMvc.perform(post("/api/admin/lending/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content1)
                                .header("Authorization", "Bearer " + token));

                // 청구기호로 대출 정보 조회
                Optional<Lending> lending =
                        lendingRepository.findByBookIdAndReturningLibrarianIsNull(book1Id);

                // 반납 처리
                mockMvc.perform(put("/api/admin/lending/returning")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"lendingId\":" + lending.get().getId() + ", " +
                                        "\"returningCondition\":\"테스트트\"}")
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
            @DisplayName("반납하는 도서가 대출 중이 아닐 때 예외처리")
            public void returnLendingWhenBookDoesNotExist() throws Exception {
                mockMvc.perform(put("/api/admin/lending/returning")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"lendingId\":100, " +
                                        "\"returningCondition\":\"테스트트\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
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
                mockMvc.perform(get("/api/admin/lending/all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "최신도서")
                                .param("role", "전체")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            }
        }
    }
}
