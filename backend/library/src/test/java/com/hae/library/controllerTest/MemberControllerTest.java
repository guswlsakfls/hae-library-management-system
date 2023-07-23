package com.hae.library.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.domain.Enum.Role;
import com.hae.library.dto.Lending.Request.RequestLendingDto;
import com.hae.library.dto.Member.Request.RequestChangeMemberInfoDto;
import com.hae.library.dto.Member.Request.RequestSignupDto;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.CategoryRepository;
import com.hae.library.repository.MemberRepository;
import com.hae.library.service.MemberService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("MemberController 통합 테스트")
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Autowired
    private BookRepository bookRepository;

    private String token;
    private Book book;

    @BeforeEach
    public void setup() throws Exception {
        // 테스트용 회원 데이터를 생성합니다.
        for (int i = 1; i <= 2; i++) {
            RequestSignupDto requestSignupDto = RequestSignupDto.builder()
                    .email("tes" + i + "@gmail.com")
                    .password("test1234")
                    .checkPassword("test1234")
                    .build();
            memberService.signup(requestSignupDto);
        }

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

        bookRepository.save(Book.builder()
                .callSign("111.111-11-11.c1")
                .bookInfo(bookInfo)
                .status(BookStatus.valueOf("FINE"))
                .lendingStatus(false)
                .donator("테스트 기증자")
                .build());
    }

    @Nested
    @DisplayName("회원 가입")
    public class SignupTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Nested
            @DisplayName("회원 가입 성공")
            public class SignupSuccessTest {
                @Test
                public void signUpTest() throws Exception {
                    // Given
                    String requestUrl = "/api/signup";
                    String email = "test1234@gmail.com";
                    String password = "test1234";
                    String checkPassword = "test1234";

                    String content = String.format("{\"email\":\"%s\", \"password\":\"%s\", \"checkPassword\":\"%s\"}",
                            email, password, checkPassword);

                    // When
                    ResultActions resultActions = mockMvc.perform(post(requestUrl)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content));

                    // Then
                    resultActions.andExpect(status().isOk());
                }

            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("이미 존재하는 회원으로 가입 시도")
            public void alreadyExistMemberTest() throws Exception {
                // Given
                RequestSignupDto requestSignupDto = RequestSignupDto.builder()
                        .email("guswls@gmail.com")
                        .password("test123423f23f")
                        .checkPassword("test123423f23f")
                        .build();

                // When: 먼저 이메일이 존재하지 않을 때 회원가입 요청을 보낸다.
                mockMvc.perform(post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestSignupDto)))
                        .andExpect(status().isOk());

                // Then: 같은 이메일로 다시 회원가입 요청을 보내면 이미 존재하는 이메일이므로 예외가 발생해야 한다.
                mockMvc.perform(post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestSignupDto)))
                        .andExpect(status().isConflict())
                        .andDo(print());
            }

            @Test
            @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않을 때")
            public void passwordNotMatchTest() throws Exception {
                // Given
                RequestSignupDto requestSignupDto = RequestSignupDto.builder()
                        .email("admin@gmail.com")
                        .password("test123423f23f")
                        .checkPassword("test123423")
                        .build();

                // when
                ResultActions resultActions = mockMvc.perform(post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestSignupDto)));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message", is("비밀번호가 일치하지 않습니다")))
                        .andDo(print());
            }
        }
    }

    @Nested
    @DisplayName("회원 조회")
    public class FindMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("모든 회원 조회 시 정보 반환")
            public void findAllMemberTest() throws Exception {
                // Given
                String requestUrl = "/api/admin/memberinfo/all";
                String page = "0";
                String size = "10";
                String sort = "최신순";
                String role = "전체";
                String authorizationHeader = "Bearer " + token;

                // When
                ResultActions resultActions = mockMvc.perform(get(requestUrl)
                        .param("page", page)
                        .param("size", size)
                        .param("sort", sort)
                        .param("role", role)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.memberList", hasSize(3)))
                        .andDo(print());
            }

            @Test
            @DisplayName("내 프로필 정보를 조회 시 내 정보 반환")
            public void findMyProfileTest() throws Exception {
                // Given
                String requestUrl = "/api/member/memberinfo/me";
                String authorizationHeader = "Bearer " + token;
                String expectedEmail = "admin@gmail.com";

                // When
                ResultActions resultActions = mockMvc.perform(get(requestUrl)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.email", is(expectedEmail)))
                        .andDo(print());
            }

            @Test
            @DisplayName("특정 회원 조회 시 정보 반환")
            public void findMemberTest() throws Exception {
                // Given
                String requestUrl = "/api/admin/memberinfo";
                String authorizationHeader = "Bearer " + token;
                String requestBody = "{\"email\":\"admin@gmail.com\"}";
                String expectedEmail = "admin@gmail.com";

                // When
                ResultActions resultActions = mockMvc.perform(post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", authorizationHeader));

                // Then
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.email", is(expectedEmail)))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 회원 조회 시 예외 발생")
            public void notExistMemberTest() throws Exception {
                // Given
                String requestUrl = "/api/admin/memberinfo";
                String email = "notExisted@gmail.com";
                String requestBody = String.format("{\"email\":\"%s\"}", email);

                // When
                ResultActions resultActions = mockMvc.perform(post(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andDo(print());

                // Then
                resultActions.andExpect(jsonPath("$.message", is("회원을 찾을 수 없습니다")));
            }
        }
    }

    @Nested
    @DisplayName("회원 수정")
    public class UpdateMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("회원 정보 수정 시 정보 수정")
            public void updateMemberTest() throws Exception {
                // Given
                String requestUrl = "/api/admin/member";
                RequestChangeMemberInfoDto requestChangeMemberInfoDto = RequestChangeMemberInfoDto.builder()
                        .id(1L)
                        .email("change@gmail.com")
                        .activated(true)
                        .role(Role.valueOf("ROLE_USER"))
                        .build();
                String requestChangeMemberInfoDtoJson = new ObjectMapper().writeValueAsString(requestChangeMemberInfoDto);

                // When
                ResultActions resultActions = mockMvc.perform(put(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestChangeMemberInfoDtoJson)
                                .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.email", is("change@gmail.com"))) // 응답 데이터 검증
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("변경하려는 이메일이 중복되는 경우 예외 발생")
            public void duplicateEmailTest() throws Exception {
                // Given
                String requestUrl = "/api/admin/member";
                RequestChangeMemberInfoDto requestChangeMemberInfoDto = RequestChangeMemberInfoDto.builder()
                        .id(1L)
                        .email("tes1@gmail.com")
                        .activated(true)
                        .role(Role.valueOf("ROLE_USER"))
                        .build();

                String requestChangeMemberInfoDtoJson = new ObjectMapper().writeValueAsString(requestChangeMemberInfoDto);

                // When
                ResultActions resultActions = mockMvc.perform(put(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestChangeMemberInfoDtoJson)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andDo(print());

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message", is("이미 존재하는 이메일입니다")));
            }
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 및 삭제")
    public class DeleteMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Nested
            @DisplayName("회원 휴면계정 전환")
            public class InactivateMemberTest {
                @Test
                @DisplayName("id에 해당하는 회원 휴면계정 전환")
                public void inactivateMemberTest() throws Exception {
                    // Given
                    String requestUrl = "/api/member/withdrawal/me";

                    // When
                    ResultActions resultActions = mockMvc.perform(put(requestUrl)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + token));

                    // Then
                    resultActions.andExpect(status().isOk())
                            .andDo(print());
                }
            }

            @Test
            @DisplayName("id에 해당하는 회원 삭제")
            public void deleteMemberTest() throws Exception {
                // Given
                long memberId = 1L;
                String requestUrl = "/api/admin/member/" + memberId;

                // When
                ResultActions resultActions = mockMvc.perform(delete(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isOk())
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("대출이 있는 회원은 휴면계정 처리시 예외 발생")
            public void existLoanTest() throws Exception {
                // Given
                String requestUrl = "/api/member/withdrawal/me";

                // 대출 중인 도서가 있는 회원으로 변경
                RequestLendingDto requestLendingDto = RequestLendingDto.builder()
                        .bookId(1L)
                        .userId(1L)
                        .lendingCondition("이상없음")
                        .build();

                mockMvc.perform(post("/api/admin/lending")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestLendingDto))
                        .header("Authorization", "Bearer " + token));

                // When
                ResultActions resultActions = mockMvc.perform(put(requestUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message", is("대출중인 도서가 있습니다")))
                        .andDo(print());
            }

            @Test
            @DisplayName("존재하지 않는 회원 삭제 시 예외 발생")
            public void notExistMemberTest() throws Exception {
                // Given
                long memberId = 1000L;
                String requestUrl = "/api/admin/member/" + memberId;

                // When
                ResultActions resultActions = mockMvc.perform(delete(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token));

                // Then
                resultActions.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message", is("회원을 찾을 수 없습니다")))
                        .andDo(print());
            }
        }
    }
}
