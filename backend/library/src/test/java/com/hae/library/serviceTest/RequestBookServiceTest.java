package com.hae.library.serviceTest;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Member;
import com.hae.library.domain.RequestBook;
import com.hae.library.dto.BookInfo.Request.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.Response.ResponseBookInfoDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.MemberRepository;
import com.hae.library.repository.RequestBookRepository;
import com.hae.library.service.BookInfoService;
import com.hae.library.service.RequestBookService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestBookServiceTest {
    @Mock
    private RequestBookRepository requestBookRepo;

    @Mock
    private MemberRepository memberRepo;

    @Mock
    private BookInfoRepository bookInfoRepo;

    @Mock
    private BookInfoService bookInfoService;

    @InjectMocks
    private RequestBookService requestBookService;

    private BookInfo bookInfo;
    private Member member;

    @BeforeEach
    public void setUp() {
        Category category = Category.builder()
                .categoryName("test")
                .build();
        bookInfo = BookInfo.builder()
                .isbn("9791168473690")
                .title("Test Book")
                .author("John Doe")
                .publisher("Test Publisher")
                .image("test.jpg")
                .publishedAt("2023-06-30")
                .category(category)
                .build();
        member = Member.builder()
                .email("admin@gmail.com")
                .password("1234")
                .role(Role.valueOf("ROLE_ADMIN"))
                .build();
    }

    @Nested
    @DisplayName("도서 구매 요청")
    public class RequestBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 구매 요청 정보를 입력받아 저장")
            public void requestBook() {
                // Given
                RequestBookInfoDto requestBookInfoDto = RequestBookInfoDto.builder()
                        .isbn("9791168473690")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .categoryName("test")
                        .build();

                // SecurityContext 설정
                // 대출 사서 정보를 SecurityContext에 설정
                UserDetails userDetails = User.withDefaultPasswordEncoder()
                        .username("admin@gmail.com")
                        .password("password")
                        .roles("USER")
                        .build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                when(memberRepo.findByEmail("admin@gmail.com")).thenReturn(Optional.of(member));
                when(bookInfoService.createBookInfo(any(RequestBookInfoDto.class))).thenReturn(bookInfo);

                // When
                requestBookService.createRequestBook(requestBookInfoDto);

                // Then
                verify(requestBookRepo, times(1)).save(any());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("이미 존재하는 도서인 경우 예외 발생")
            public void requestBook() {
                // Given
                RequestBookInfoDto requestBookInfoDto = RequestBookInfoDto.builder()
                        .isbn("9791168473690")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .categoryName("test")
                        .build();

                // SecurityContext 설정
                // 대출 사서 정보를 SecurityContext에 설정
                UserDetails userDetails = User.withDefaultPasswordEncoder()
                        .username("admin@gmail.com")
                        .password("password")
                        .roles("USER")
                        .build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                when(bookInfoService.createBookInfo(any(RequestBookInfoDto.class))).thenThrow(new RestApiException(BookErrorCode.DUPLICATE_ISBN));
                when(memberRepo.findByEmail("admin@gmail.com")).thenReturn(Optional.of(member));


                // then
                assertThrows(RestApiException.class, () -> {
                    // when
                    requestBookService.createRequestBook(requestBookInfoDto);
                });
            }
        }
    }

    @Nested
    @DisplayName("구매 요청 도서 정보 조회")
    public class GetRequestBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("구매 요청 도서 정보 조회")
            public void getRequestBook() {
                // given
                int page = 0;
                int size = 10;
                String search = "test";
                String categoryName = "분야";
                String sort = "최신도서";
                String approved = "구매완료";

                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

                RequestBook requestBook = RequestBook.builder()
                        .bookInfo(bookInfo)
                        .member(member)
                        .isApproved(false)
                        .build();

                Page<RequestBook> pageRequestBook =
                        new PageImpl<>(Collections.singletonList(requestBook), pageable, 1);

                when(requestBookRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageRequestBook);

                // when
                Page<ResponseBookInfoDto> result = requestBookService.getRequestBookList(search, page, size, categoryName, sort, approved);

                // then
                assertEquals(1, result.getTotalElements());
                verify(requestBookRepo, times(1)).findAll(any(Specification.class), any(Pageable.class));
            }
        }
    }

    @Nested
    @DisplayName("구매 요청 도서 삭제")
    public class DeleteRequestBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("구매 요청 도서 삭제")
            public void deleteRequestBook() {
                // given
                Long id = 1L;

                RequestBook requestBook = RequestBook.builder()
                        .bookInfo(bookInfo)
                        .member(member)
                        .isApproved(false)
                        .build();

                when(requestBookRepo.findById(id)).thenReturn(Optional.of(requestBook));

                // when
                requestBookService.deleteRequestBook(id);

                // then
                verify(requestBookRepo, times(1)).delete(requestBook);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 구매 요청 도서 삭제")
            public void deleteRequestBook() {
                // given
                Long id = 1L;

                when(requestBookRepo.findById(id)).thenReturn(Optional.empty());

                // then
                assertThrows(RestApiException.class, () -> {
                    // when
                    requestBookService.deleteRequestBook(id);
                });
            }
        }
    }
}
