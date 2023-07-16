package com.hae.library.controllerTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hae.library.domain.Enum.Role;
import com.hae.library.dto.Member.Request.RequestChangeMemberInfoDto;
import com.hae.library.dto.Member.Request.RequestSignupDto;
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

    private String token;

    @BeforeEach
    public void setup() throws Exception {
        // 테스트용 회원 데이터를 생성합니다.
        for (int i = 1; i <= 10; i++) {
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

        System.out.println("loginResponse = " + loginResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(loginResponse);
        this.token = responseJson.get("data").get("accessToken").asText();
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
                    RequestSignupDto requestSignupDto = RequestSignupDto.builder()
                            .email("test1234@gmail.com")
                            .password("test1234")
                            .checkPassword("test1234")
                            .build();

                    // When & Then
                    mockMvc.perform(post("/api/signup")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(new ObjectMapper().writeValueAsString(requestSignupDto)))
                            .andExpect(status().isOk())
                            .andDo(print());
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

                // Then: 같은 이메일로 다시 회원가입 요청을 보내면 이미 존재하는 이메일이므로 예외가 발생해야 한다.
                mockMvc.perform(post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(requestSignupDto)))
                        .andExpect(status().isBadRequest())
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
                mockMvc.perform(get("/api/admin/memberinfo/all")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "최신순")
                                .param("role", "전체")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.memberList", hasSize(10)))
                        .andDo(print());
            }

            @Test
            @DisplayName("내 프로필 정보를 조회 시 내 정보 반환")
            public void findMyProfileTest() throws Exception {
                mockMvc.perform(get("/api/member/memberinfo/me")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.email", is("admin@gmail.com")))
                        .andDo(print());
            }

            @Test
            @DisplayName("특정 회원 조회 시 정보 반환")
            public void findMemberTest() throws Exception {
                mockMvc.perform(post("/api/admin/memberinfo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"admin@gmail.com\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.data.email", is("admin@gmail.com")))
                        .andDo(print());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 회원 조회 시 예외 발생")
            public void notExistMemberTest() throws Exception {
                mockMvc.perform(post("/api/admin/memberinfo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"notExisted@gmail.com\"}")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andDo(print());
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
                // 테스트 데이터 생성
                RequestChangeMemberInfoDto requestChangeMemberInfoDto = RequestChangeMemberInfoDto.builder()
                        .id(1L)
                        .email("change@gmail.com")
                        .activated(true)
                        .role(Role.valueOf("ROLE_USER"))
                        .build();

                String requestChangeMemberInfoDtoJson = new ObjectMapper().writeValueAsString(requestChangeMemberInfoDto);

                // PUT 요청 실행
                mockMvc.perform(put("/api/admin/member/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestChangeMemberInfoDtoJson)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
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
                // 테스트 데이터 생성
                RequestChangeMemberInfoDto requestChangeMemberInfoDto = RequestChangeMemberInfoDto.builder()
                        .id(1L)
                        .email("tes1@gmail.com")
                        .activated(true)
                        .role(Role.valueOf("ROLE_USER"))
                        .build();

                String requestChangeMemberInfoDtoJson = new ObjectMapper().writeValueAsString(requestChangeMemberInfoDto);

                // PUT 요청 실행
                mockMvc.perform(put("/api/admin/member/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestChangeMemberInfoDtoJson)
                                .header("Authorization", "Bearer " + token))
                        .andExpect(status().isBadRequest())
                        .andDo(print());
            }
        }
    }
}
