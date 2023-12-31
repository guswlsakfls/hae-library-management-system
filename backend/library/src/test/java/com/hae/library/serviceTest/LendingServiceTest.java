package com.hae.library.serviceTest;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import com.hae.library.dto.Lending.Request.RequestLendingDto;
import com.hae.library.dto.Lending.Request.RequestReturningDto;
import com.hae.library.dto.Lending.Response.ResponseLendingDto;
import com.hae.library.dto.Lending.Response.ResponseMemberLendingDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.global.Exception.errorCode.MemberErrorCode;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.LendingRepository;
import com.hae.library.repository.MemberRepository;
import com.hae.library.service.LendingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LendingServiceTest {
    @Mock
    private MemberRepository memberRepo;

    @Mock
    private BookRepository bookRepo;

    @Mock
    private LendingRepository lendingRepo;

    @InjectMocks
    private LendingService lendingService;


    private Lending lending;
    private Member lendingUser;
    private Member lendingLibrarian;
    private Member returningLibrarian;
    private BookInfo bookInfo;
    private Book book;

    @BeforeEach
    public void setUp() {
        // SecurityContext 설정
        // 대출 사서 정보를 SecurityContext에 설정
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("admin@gmail.com")
                .password("password")
                .roles("USER")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        lendingUser = Member.builder()
                .email("hyujo@gmail.com")
                .password("1234")
                .role(Role.ROLE_USER)
                .activated(true)
                .build();
        lendingLibrarian = Member.builder()
                .email("admin@gmail.com")
                .password("1234")
                .role(Role.ROLE_ADMIN)
                .build();
        returningLibrarian = Member.builder()
                .email("admin2@gmail.com")
                .password("1234")
                .role(Role.ROLE_ADMIN)
                .build();

        bookInfo = BookInfo.builder()
                .title("자바의 정석")
                .author("남궁성")
                .publisher("도우출판사")
                .build();

        book = Book.builder()
                .callSign("100.10.v1.c1")
                .status(BookStatus.FINE)
                .lendingStatus(false)
                .bookInfo(bookInfo)
                .build();

        String lendingCondition = "이상없음";
        lending = Lending.builder()
                .lendingUser(lendingUser)
                .lendingLibrarian(lendingLibrarian)
                .lendingCondition(lendingCondition)
                .build();
        lending.setCreatedAt(LocalDateTime.now());
        lending.setUpdatedAt(LocalDateTime.now());
        lending.addBook(book);
    }

    @Nested
    @DisplayName("대출 정보 생성")
    public class CreateLendingTest {

        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("도서 대출에 대한 정보를 입력받아 대출 정보를 생성")
            public void lendingBookTest() {
                // Given
                RequestLendingDto request = RequestLendingDto.builder()
                        .userId(lendingUser.getId())
                        .bookId(book.getId())
                        .lendingCondition(lending.getLendingCondition())
                        .build();

                // When
                // 대출 사서 객체가 레포지토리에서 반환되도록 합니다.
                when(memberRepo.findByEmail("admin@gmail.com")).thenReturn(Optional.of(lendingLibrarian));
                when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));
                when(memberRepo.findById(lendingUser.getId())).thenReturn(Optional.of(lendingUser));

                // 대출 처리 실행
                lendingService.lendingBook(request);

                // Then
                // lending값이 저장 되었는지 결과를 검증합니다.
                verify(lendingRepo, times(1)).save(any(Lending.class));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("존재하지 않은 책을 대출하려고 할 때 예외 처리")
            public void lendingBookTest_BookNotFound() {
                // Given
                Long nonexistentBookId = 100L;
                RequestLendingDto request = RequestLendingDto.builder()
                        .userId(lendingUser.getId())
                        .bookId(nonexistentBookId)
                        .lendingCondition(lending.getLendingCondition())
                        .build();
                when(bookRepo.findById(nonexistentBookId)).thenReturn(Optional.empty());

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.lendingBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(BookErrorCode.BAD_REQUEST_BOOKINFO);
            }

            @Test
            @DisplayName("이미 대출된 책을 대출하려고 할 때 예외 처리")
            public void lendingBookTest_BookAlreadyLent() {
                // Given
                RequestLendingDto request = RequestLendingDto.builder()
                        .userId(lendingUser.getId())
                        .bookId(book.getId())
                        .lendingCondition(lending.getLendingCondition())
                        .build();
                book.updateLendingStatus();
                when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.lendingBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(BookErrorCode.BOOK_ALREADY_LENT);
            }

            @Test
            @DisplayName("분실 책을 대출할 때 예외 처리")
            public void lendingBookTest_BookLost() {
                // Given
                RequestLendingDto request = RequestLendingDto.builder()
                        .userId(lendingUser.getId())
                        .bookId(book.getId())
                        .lendingCondition(lending.getLendingCondition())
                        .build();
                book.updateBookStatus(BookStatus.LOST);
                when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.lendingBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(BookErrorCode.BOOK_LOST);
            }

            @Test
            @DisplayName("대출 받는 유저가 회원인지 확인 예외처리")
            public void lendingBookTest_UserNotFound() {
                // Given
                Long nonexistentUserId = 100L;
                RequestLendingDto request = RequestLendingDto.builder()
                        .userId(nonexistentUserId)
                        .bookId(book.getId())
                        .lendingCondition(lending.getLendingCondition())
                        .build();
                when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));
                when(memberRepo.findById(nonexistentUserId)).thenReturn(Optional.empty());

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.lendingBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.USER_NOT_FOUND);
            }

            @Test
            @DisplayName("대출 받는 비활성된 유저 예외처리")
            public void lendingBookTest_UserNotActive() {
                // Given
                RequestLendingDto request = RequestLendingDto.builder()
                        .userId(lendingUser.getId())
                        .bookId(book.getId())
                        .lendingCondition(lending.getLendingCondition())
                        .build();
                lendingUser.updateActivated(false);
                when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));
                when(memberRepo.findById(lendingUser.getId())).thenReturn(Optional.of(lendingUser));

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.lendingBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.INACTIVE_MEMBER);
            }

            @Test
            @DisplayName("대출 권수(3권)를 초과할 때 예외 처리")
            public void lendingBookTest_OverLendingCount() {
                // Given
                RequestLendingDto request = RequestLendingDto.builder()
                        .userId(lendingUser.getId())
                        .bookId(book.getId())
                        .lendingCondition(lending.getLendingCondition())
                        .build();

                // lendingCount만큼 lendingUser의 대출 권수를 증가시킴
                int lendingCount = 4; // 대출 권수 설정
                for (int i = 0; i < lendingCount; i++) {
                    lendingUser.increaseLendingCount();
                }
                when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));
                when(memberRepo.findById(lendingUser.getId())).thenReturn(Optional.of(lendingUser));

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.lendingBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.OVER_LENDING_COUNT);
            }

            @Test
            @DisplayName("대출 받은 유저가 연체일이 있을 때 예외 처리")
            public void lendingBookTest_UserHasOverdue() {
                // Given
                RequestLendingDto request = RequestLendingDto.builder()
                        .userId(lendingUser.getId())
                        .bookId(book.getId())
                        .lendingCondition(lending.getLendingCondition())
                        .build();
                lendingUser.updatePenaltyEndDate(LocalDate.now().plusDays(1).atStartOfDay());
                when(bookRepo.findById(book.getId())).thenReturn(Optional.of(book));
                when(memberRepo.findById(lendingUser.getId())).thenReturn(Optional.of(lendingUser));

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.lendingBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(MemberErrorCode.USER_OVERDUE);
            }
        }
    }

    @Nested
    @DisplayName("대출 반납")
    public class ReturningBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("대출된 도서 반납")
            public void returningBookTest_Success() {
                // Given
                // 대출된 도서와 대출자를 spy로 생성합니다.
                // 이들은 실제 객체지만 일부 메서드의 반환값을 조작하거나 호출을 추적할 수 있습니다.
                Book bookSpy = spy(book);
                Member lendingUserSpy = spy(lendingUser);

                String lendingCondition = "이상없음";
                // 반납할 대출 정보를 생성합니다.
                Lending returningLending = Lending.builder()
                        .lendingUser(lendingUserSpy)
                        .lendingLibrarian(lendingLibrarian)
                        .lendingCondition(lendingCondition)
                        .build();
                // 대출 정보에 대출된 도서를 추가합니다.
                returningLending.addBook(bookSpy);
                // 대출 정보도 spy로 생성합니다.
                Lending lendingSpy = spy(returningLending);
                lendingSpy.setCreatedAt(LocalDateTime.now().minusDays(1));
                lendingSpy.setUpdatedAt(LocalDateTime.now().minusDays(1));

                // 도서를 대출 처리 합니다.
                bookSpy.updateLendingStatus();
                lendingSpy.addBook(bookSpy);

                // 반납 요청 정보를 생성합니다.
                RequestReturningDto request = RequestReturningDto.builder()
                        .lendingId(lending.getId())
                        .returningCondition("Good")
                        .build();

                // Authentication 객체를 생성하고 SecurityContext에 설정하여
                // 반납 담당 사서가 로그인된 상황을 시뮬레이션합니다.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(returningLibrarian.getEmail(), returningLibrarian.getPassword());
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);


                // 대출 담당 사서가 로그인되어 있다고 가정
                when(memberRepo.findByEmail(returningLibrarian.getEmail())).thenReturn(Optional.of(returningLibrarian));

                // 대출 정보를 반환
                when(lendingRepo.findById(lendingSpy.getId())).thenReturn(Optional.of(lendingSpy));

                // When
                // 반납 처리를 시도합니다.
                // 이 때 예외가 발생하지 않아야 하므로 assertDoesNotThrow를 사용합니다.
                assertDoesNotThrow(() -> lendingService.returningBook(request), "성공적으로 책을 반납해야 합니다.");

                // Then
                // 도서의 반납 상태를 확인합니다.
                verify(bookSpy, times(1)).updateReturningStatus();

                // 유저의 대출 횟수 감소를 확인합니다.
                verify(returningLending.getLendingUser(), times(1)).decreaseLendingCount();

                // 반납 처리 정보 업데이트를 확인합니다.
                verify(lendingSpy, times(1)).updateReturning(any(), anyString(), any());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("대출 정보가 존재하지 않은 경우 예외 발생")
            public void returningBookTest_NoBook() {
                // Given
                // 반납 요청 정보를 생성합니다.
                RequestReturningDto request = RequestReturningDto.builder()
                        .lendingId(9999L)  // 존재하지 않는 대출 정보의 ID를 설정합니다.
                        .returningCondition("Good")
                        .build();

                // When
                // 반납 처리를 시도하면서 예외가 발생해야 합니다.
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.returningBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(BookErrorCode.BAD_REQUEST_LENDING);
            }

            @Test
            @DisplayName("도서가 대출 중이 아닌 경우 예외 발생")
            public void returningBookTest_NotLending() {
                // Given
                Book bookSpy = spy(book);
                bookSpy.updateReturningStatus();  // 이미 반납 처리된 도서로 설정합니다.

                Lending returningLending = Lending.builder()
                        .lendingUser(lendingUser)
                        .lendingLibrarian(lendingLibrarian)
                        .lendingCondition("Good")
                        .build();
                returningLending.addBook(bookSpy);
                Lending lendingSpy = spy(returningLending);

                RequestReturningDto request = RequestReturningDto.builder()
                        .lendingId(lendingSpy.getId())
                        .returningCondition("Good")
                        .build();

                // Authentication 객체를 생성하고 SecurityContext에 설정하여
                // 반납 담당 사서가 로그인된 상황을 시뮬레이션합니다.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(returningLibrarian.getEmail(), returningLibrarian.getPassword());
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);

                // 대출 담당 사서가 로그인되어 있다고 가정
                when(memberRepo.findByEmail(returningLibrarian.getEmail())).thenReturn(Optional.of(returningLibrarian));
                when(lendingRepo.findById(lendingSpy.getId())).thenReturn(Optional.of(lendingSpy));

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> lendingService.returningBook(request));

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(BookErrorCode.NOT_LENDING_BOOK);
            }
        }
    }

    @Nested
    @DisplayName("대출 기록 조회")
    public class getBookHistory {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("관리자가 모든 대출 기록을 페이징하여 조회")
            public void getAllLendingHistoryTest_Success() {
                // Given
                int page = 0;
                int size = 5;
                String isLendingOrReturning = "전체";
                String sort = "최신순";
                String search = null;  // 검색어는 없음.

                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

                // 대출 기록을 생성합니다.
                List<Lending> lendingList = Arrays.asList(
                        lending, lending
                );

                Page<Lending> lendingPage = new PageImpl<>(lendingList, pageable, lendingList.size());

                // 페이징된 대출 기록을 반환합니다.
                when(lendingRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(lendingPage);

                // When
                Page<ResponseLendingDto> result = lendingService.getAllLendingHistory(search, page, size, isLendingOrReturning, sort);

                // Then
                assertEquals(lendingPage.getTotalElements(), result.getTotalElements());
            }

            @Test
            @DisplayName("회원 자신의 대출 기록을 페이징하여 조회")
            public void getLendingHistoryTest_Success() {
                // Given
                int page = 0;
                int size = 5;
                String isLendingOrReturning = "전체";
                String sort = "최신순";
                String search = null;  // 검색어는 없음.
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

                // 대출 기록을 생성합니다.
                List<Lending> lendingList = Arrays.asList(
                        lending, lending
                );

                // Authentication 객체를 생성하고 SecurityContext에 설정하여
                // 반납 담당 사서가 로그인된 상황을 시뮬레이션합니다.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(returningLibrarian.getEmail(), returningLibrarian.getPassword());
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);

                // 대출 담당 사서가 로그인되어 있다고 가정
                when(memberRepo.findByEmail(returningLibrarian.getEmail())).thenReturn(Optional.of(returningLibrarian));

                Page<Lending> lendingPage = new PageImpl<>(lendingList, pageable, lendingList.size());

                // 페이징된 대출 기록을 반환합니다.
                when(lendingRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(lendingPage);

                // When
                Page<ResponseMemberLendingDto> result =
                        lendingService.getMemberLendingHistory(search,
                        page,
                        size, isLendingOrReturning, sort);

                // Then
                assertEquals(lendingPage.getTotalElements(), result.getTotalElements(), "전체 대출 기록 수는 일치해야 합니다.");
            }
        }
    }
    @Nested
    @DisplayName("대출 기록 삭제")
    public class deleteLendingHistory {
        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("id에 해당하는 대출 기록을 찾아 삭제")
            public void deleteLendingHistoryTest_Success() {
                // Given
                // 대출 기록을 생성합니다.
                Long lendingId = 1L;

                Lending lending = Lending.builder()
                        .id(lendingId)
                        .lendingUser(lendingUser)
                        .lendingLibrarian(lendingLibrarian)
                        .lendingCondition("Good")
                        .build();
                lending.addBook(book);

                // lendingRepo.findById() 가 호출되었을 때 Optional.empty()를 반환하도록 설정합니다.
                when(lendingRepo.findById(any(Long.class))).thenReturn(Optional.of(lending));

                // When
                lendingService.deleteLending(lending.getId());

                // Then
                verify(lendingRepo, times(1)).delete(any(Lending.class));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("삭제할 id의 대출 기록을 찾지 못하는 경우")
            public void deleteLendingHistoryTest_Fail_LendingHistoryNotFound() {
                // Given
                Long lendingId = 1L;

                // When & Then
                assertThrows(RestApiException.class, () -> lendingService.deleteLending(lendingId));
            }
        }
    }
}
