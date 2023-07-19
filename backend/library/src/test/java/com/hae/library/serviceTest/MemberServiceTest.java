package com.hae.library.serviceTest;

import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Member;
import com.hae.library.dto.Member.Request.RequestChangeMemberInfoDto;
import com.hae.library.dto.Member.Request.RequestChangePasswordDto;
import com.hae.library.dto.Member.Request.RequestEmailDto;
import com.hae.library.dto.Member.Request.RequestSignupDto;
import com.hae.library.dto.Member.Response.ResponseMemberDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.repository.MemberRepository;
import com.hae.library.service.MemberService;
import com.hae.library.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 테스트")
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private MemberService memberService;

    private RequestSignupDto requestSignupDto;

    @BeforeEach
    public void setup() {
        requestSignupDto = RequestSignupDto.builder()
                .email("test@gmail.com")
                .password("1234")
                .checkPassword("1234")
                .build();
    }

    @Nested
    @DisplayName("회원 가입")
    public class SignupTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("비밀번호와 비밀번호 재확인이 일치하고, 이메일이 중복으로 존재하지 않을 때 회원가입이 성공한다")
            public void signupSuccessTest() {
                // Given
                RequestSignupDto requestSignupDto = RequestSignupDto.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .checkPassword("1234")
                        .build();

                // 이미 존재하는 이메일을 찾지 못하도록 설정하고, 새로 생성된 회원을 저장하도록 설정합니다.
                when(memberRepository.findByEmail(requestSignupDto.getEmail())).thenReturn(Optional.empty());
                when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // When
                memberService.signup(requestSignupDto);

                // Then
                verify(memberRepository).save(any(Member.class));
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("비밀번호와 비밀번호 재확인이 일치하지 않을 때 회원가입이 실패 예외 발")
            public void signupFailTestPasswordNotMatch() {
                // Given
                RequestSignupDto requestSignupDto = RequestSignupDto.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .checkPassword("12345")
                        .build();

                // When & Then
                assertThrows(RestApiException.class, () -> memberService.signup(requestSignupDto));
            }

            @Test
            @DisplayName("중복 이메일로 회원가입 시도 시 예외 발생") // TODO: data.sql.sql에 admin@gmail이 존재해야 함
            public void shouldThrowExceptionWhenSignupWithDuplicatedEmail() {
                // Given
                RequestSignupDto requestSignupDto = RequestSignupDto.builder()
                        .email("admin@gmail.com")
                        .password("1234")
                        .checkPassword("1234")
                        .build();

                Member member = Member.builder()
                        .email(requestSignupDto.getEmail())
                        .password(requestSignupDto.getPassword())
                        .build();

                when(memberRepository.findByEmail(requestSignupDto.getEmail())).thenReturn(Optional.of(member));

                // When & Then
                Exception exception = assertThrows(RestApiException.class, () -> memberService.signup(requestSignupDto));
                assertEquals(MemberErrorCode.MEMBER_ALREADY_EXIST, ((RestApiException) exception).getErrorCode());
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
            @DisplayName("회원 목록을 검색어와 페이징조건으로 조회")
            public void getAllMemberTest() {
                // Given
                String search = "test";
                int page = 0;
                int size = 10;
                String role = "전체";
                String sort = "최신순";

                // 예상되는 반환 값인 회원 목록
                List<Member> memberList = new ArrayList<>();
                Member test1 = Member.builder()
                        .id(1L)
                        .email("test1@gamil.com")
                        .build();
                test1.setCreatedAt(LocalDateTime.now().minusDays(1));
                test1.setUpdatedAt(LocalDateTime.now().minusDays(1));
                memberList.add(test1);

                Member test2 = Member.builder()
                        .id(2L)
                        .email("test2@gmai.com")
                        .build();
                test2.setCreatedAt(LocalDateTime.now().minusDays(2));
                test2.setUpdatedAt(LocalDateTime.now().minusDays(2));
                memberList.add(test2);

                Member test3 = Member.builder()
                        .id(3L)
                        .email("admin@gmai.com")
                        .build();
                test3.setCreatedAt(LocalDateTime.now());
                test3.setUpdatedAt(LocalDateTime.now());
                memberList.add(test3);

                Page<Member> memberPage = new PageImpl<>(memberList);

                when(memberRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(memberPage);

                // When
                Page<ResponseMemberDto> result = memberService.getAllMember(search, page, size, role, sort);

                // Then
                assertEquals(memberList.size(), result.getContent().size()); // 회원 수 검증
                assertEquals(memberList.get(0).getEmail(), result.getContent().get(0).getEmail()); // 첫 번째 회원 이메일 검증
                assertEquals(memberList.get(1).getEmail(), result.getContent().get(1).getEmail()); // 두 번째 회원 이메일 검증
            }

            @Test
            @DisplayName("검색어가 비어있을 때 모든 회원 목록을 페이징하여 조회")
            public void getAllMemberTest_WithEmptySearch() {
                // Given
                String search = "";
                int page = 0;
                int size = 10;
                String role = "전체";
                String sort = "최신순";

                // 예상되는 반환 값인 회원 목록
                List<Member> memberList = new ArrayList<>();
                Member test1 = Member.builder()
                        .id(1L)
                        .email("test1@gamil.com")
                        .build();
                test1.setCreatedAt(LocalDateTime.now().minusDays(1));
                test1.setUpdatedAt(LocalDateTime.now().minusDays(1));
                memberList.add(test1);

                Member test2 = Member.builder()
                        .id(2L)
                        .email("test2@gmai.com")
                        .build();
                test2.setCreatedAt(LocalDateTime.now());
                test2.setUpdatedAt(LocalDateTime.now());
                memberList.add(test2);

                Page<Member> memberPage = new PageImpl<>(memberList);

                // Mockito 설정
                when(memberRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(memberPage);

                // When
                Page<ResponseMemberDto> result = memberService.getAllMember(search, page, size, role, sort);

                // Then
                assertEquals(memberList.size(), result.getContent().size()); // 회원 수 검증
            }

            @Test
            @DisplayName("로그인한 사용자의 정보 조회")
            public void getMyInfoBySecurityTest() {
                // Given
                String email = "test@gmail.com";
                Member member = Member.builder()
                        .id(1L)
                        .email(email)
                        .build();

                member.setCreatedAt(LocalDateTime.now());
                member.setUpdatedAt(LocalDateTime.now());

                // Authentication 객체를 생성하고 SecurityContext에 설정
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, "password");
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);

                when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

                // When
                ResponseMemberDto result = memberService.getMyInfoBySecurity();

                // Then
                assertEquals(email, result.getEmail());

                // SecurityContext 초기화
                SecurityContextHolder.clearContext();
            }

            @Test
            @DisplayName("이메일을 이용한 회원 정보 조회")
            public void getMemberByEmailTest() {
                // Given
                String email = "test@gmail.com";
                Member member = Member.builder()
                        .id(1L)
                        .email(email)
                        .build();

                member.setCreatedAt(LocalDateTime.now());
                member.setUpdatedAt(LocalDateTime.now());

                RequestEmailDto requestEmailDto = RequestEmailDto.builder()
                        .email(email)
                        .build();

                // Mockito를 이용하여 MemberRepository의 findByEmail 메소드의 반환값을 설정합니다.
                when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

                // When
                ResponseMemberDto result = memberService.getMemberByEmail(requestEmailDto);

                // Then
                assertEquals(email, result.getEmail());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCaseTest {
            @Test
            @DisplayName("이메일로 회원을 찾지 못했을 경우 예외 발생")
            public void getMemberByEmailNotFoundTest() {
                // Given
                String email = "notfound@gmail.com";
                RequestEmailDto requestEmailDto = new RequestEmailDto(email);

                // Mockito를 이용하여 MemberRepository의 findByEmail 메소드의 반환값을 설정합니다.
                when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

                // When & Then
                assertThrows(RestApiException.class, () -> {
                    memberService.getMemberByEmail(requestEmailDto);
                });
            }

            @Test
            @DisplayName("잘못된 회원 역할을 입력했을 때 예외 발생")
            void throwExceptionWhenInvalidRole() {
                // Given
                String search = null;
                int page = 0;
                int size = 10;
                String role = "잘못된 역할";
                String sort = "최신순";

                // When & Then
                assertThrows(RestApiException.class, () -> {
                    memberService.getAllMember(search, page, size, role, sort);
                });
            }

            @Test
            @DisplayName("잘못된 정렬 조건을 입력했을 때 예외 발생")
            void throwExceptionWhenInvalidSort() {
                // Given
                String search = null;
                int page = 0;
                int size = 10;
                String role = "전체";
                String sort = "잘못된 정렬";

                // When & Then
                assertThrows(RestApiException.class, () -> {
                    memberService.getAllMember(search, page, size, role, sort);
                });
            }
        }
    }

    @Nested
    @DisplayName("회원 정보 수정")
    class UpdateMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        class SuccessCaseTest {
            @Test
            @DisplayName("회원 정보 수정")
            public void modifyMemberInfoTest() {
                // Given
                Long memberId = 1L;
                String email = "test@gmail.com";
                String newEmail = "newEmail@gmail.com";

                Member existingMember = Member.builder()
                        .id(memberId)
                        .email(email)
                        .activated(true)
                        .build();
                existingMember.setCreatedAt(LocalDateTime.now());
                existingMember.setUpdatedAt(LocalDateTime.now());

                RequestChangeMemberInfoDto requestChangeMemberInfoDto = RequestChangeMemberInfoDto.builder()
                        .id(memberId)
                        .email(newEmail)
                        .activated(true)
                        .build();

                // Mockito를 이용하여 MemberRepository의 메소드의 반환값을 설정
                when(memberRepository.existsByEmailAndIdIsNot(newEmail, memberId)).thenReturn(false);
                when(memberRepository.findById(memberId)).thenReturn(Optional.of(existingMember));
                when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // When
                ResponseMemberDto result = memberService.modifyMemberInfo(requestChangeMemberInfoDto);

                // Then
                assertEquals(newEmail, result.getEmail());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailCaseTest {
            @Test
            @DisplayName("이메일이 중복될 경우 예외 발생")
            public void modifyMemberInfoThrowExceptionWhenEmailIsDuplicated() {
                // Given
                Long memberId = 1L;
                String newEmail = "test@gmail.com";

                RequestChangeMemberInfoDto requestChangeMemberInfoDto = RequestChangeMemberInfoDto.builder()
                        .id(memberId)
                        .email(newEmail)
                        .activated(true)
                        .build();

                when(memberRepository.existsByEmailAndIdIsNot(newEmail, memberId)).thenReturn(true);

                // When & Then
                assertThrows(RestApiException.class, () -> {
                    memberService.modifyMemberInfo(requestChangeMemberInfoDto);
                });
            }

            @Test
            @DisplayName("회원을 찾지 못했을 경우 예외 발생")
            public void modifyMemberInfoThrowExceptionWhenMemberNotFound() {
                // Given
                Long memberId = 1L;
                String newEmail = "test@gmail.com";

                RequestChangeMemberInfoDto requestChangeMemberInfoDto = RequestChangeMemberInfoDto.builder()
                        .id(memberId)
                        .email(newEmail)
                        .activated(true)
                        .build();

                Mockito.when(memberRepository.findById(requestChangeMemberInfoDto.getId())).thenReturn(Optional.empty());

                // When & Then
                assertThrows(RestApiException.class, () -> {
                    memberService.modifyMemberInfo(requestChangeMemberInfoDto);
                });
            }
        }
    }

    @Nested
    @DisplayName("회원 비밀번호 변경")
    class ChangePasswordTest {
//        @Nested
//        @DisplayName("성공 케이스")
//        class SuccessCaseTest {
//            @Test
//            @DisplayName("회원 비밀번호 변경")
//            public void changePasswordTest() {
//                // Given
//                Long memberId = 1L;
//                String email = "test1@gmail.com";
//                String password = "password123";
//                String newPassword = "newPassword";
//
//                Member member = Member.builder()
//                        .id(memberId)
//                        .email(email)
//                        .role(Role.ROLE_ADMIN)
//                        .password(password)
//                        .activated(true)
//                        .build();
//
//                RequestChangePasswordDto requestChangePasswordDto = RequestChangePasswordDto.builder()
//                        .nowPassword(password)
//                        .newPassword(newPassword)
//                        .build();
//
//                // Authentication 객체를 생성하고 SecurityContext에 설정하여
//                // 자신이 로그인한 것처럼 만들어줍니다.
//                Authentication authentication =
//                        new UsernamePasswordAuthenticationToken(email, password);
//                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//                securityContext.setAuthentication(authentication);
//                SecurityContextHolder.setContext(securityContext);
//
//                when(memberRepository.findByEmail(email)).thenReturn(Optional.ofNullable(member));
//                when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//                // When
//                memberService.changeMemberPassword(requestChangePasswordDto);
//
//                // Then
//                assertEquals(newPassword, member.getPassword());
//            }
//        }
    }


    @Nested
    @DisplayName("회원 탈퇴")
    class WithDrawalMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        class SuccessCaseTest {
            @Test
            @DisplayName("회원 활성화 계정을 휴면계정으로 변경")
            public void memberWithdrawalTest() {
                // Given
                String email = "test@gmail.com";
                Member member = Member.builder()
                        .email(email)
                        .activated(true)
                        .build();

                // Mockito를 이용하여 MemberRepository의 메소드의 반환값을 설정
                when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
                when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // SecurityContext 설정
                UserDetails userDetails = User.withDefaultPasswordEncoder()
                        .username(email)
                        .password("password")
                        .roles("USER")
                        .build();
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // When
                memberService.memberWithdrawal();

                // Then
                assertFalse(member.isActivated());
            }

            @Test
            @DisplayName("회원 삭제 성공")
            public void deleteMemberTest() {
                // Given
                Long memberId = 1L;
                Member member = Member.builder()
                        .id(memberId)
                        .email("test@gmail.com")
                        .activated(true)
                        .build();

                // Mockito를 이용하여 MemberRepository의 메소드의 반환값을 설정
                when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

                // When
                memberService.deleteMember(memberId);

                // Then
                // MemberRepository의 delete 메소드가 1번 호출되었는지 검증
                verify(memberRepository, times(1)).delete(member);
            }
        }
    }
}
