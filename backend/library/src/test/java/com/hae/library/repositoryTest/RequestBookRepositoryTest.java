package com.hae.library.repositoryTest;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Member;
import com.hae.library.domain.RequestBook;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.MemberRepository;
import com.hae.library.repository.RequestBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("RequestBookRepository 단위 테스트")
public class RequestBookRepositoryTest {
    @Autowired
    private RequestBookRepository requestBookRepo;

    @Autowired
    private BookInfoRepository bookInfoRepo;

    @Autowired
    private MemberRepository memberRepo;

    private BookInfo bookInfo;
    private Member member;

    @BeforeEach
    public void setUp() {
        bookInfo = BookInfo.builder()
                .isbn("9791168473690")
                .title("Test Book")
                .author("John Doe")
                .publisher("Test Publisher")
                .image("test.jpg")
                .publishedAt("2023-06-30")
                .build();
        member = Member.builder()
                .email("admin@gmail.com")
                .password("1234")
                .role(Role.valueOf("ROLE_ADMIN"))
                .build();
    }

    @Nested
    @DisplayName("도서 구매 요청")
    public class CreateRequestBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 구매 요청 정보를 입력받아 저장")
            public void createRequestBook() {
                // Given
                RequestBook requestBook = RequestBook.builder()
                        .bookInfo(bookInfo)
                        .member(member)
                        .isApproved(false)
                        .build();

                // When
                RequestBook createdRequestBook = requestBookRepo.save(requestBook);

                // Then
                assertNotNull(createdRequestBook.getId());
                assertEquals(requestBook.getBookInfo(), createdRequestBook.getBookInfo());
                assertEquals(requestBook.getMember(), createdRequestBook.getMember());
            }
        }
    }

    @Nested
    @DisplayName("도서 구매 요청 조회")
    public class ReadRequestBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 구매 요청 정보를 입력받아 조회")
            public void readRequestBook() {
                // Given
                bookInfoRepo.save(bookInfo);
                memberRepo.save(member);

                RequestBook requestBook = RequestBook.builder()
                        .bookInfo(bookInfo)
                        .member(member)
                        .isApproved(false)
                        .build();

                // 조회시 2개 이상 데이터가 존재할 경우
                BookInfo bookInfo1 = BookInfo.builder()
                        .isbn("9791168473691")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .build();
                bookInfoRepo.save(bookInfo1);
                Member member1 = Member.builder()
                        .email("admin@gmail.com")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .password("1234")
                        .build();
                memberRepo.save(member1);

                RequestBook requestBook1 = RequestBook.builder()
                        .bookInfo(bookInfo1)
                        .member(member1)
                        .isApproved(false)
                        .build();

                // When
                requestBookRepo.save(requestBook);
                requestBookRepo.save(requestBook1);

                // Then
                assertEquals(2, requestBookRepo.findAll().size());
            }
        }
    }

    @Nested
    @DisplayName("도서 구매 요청 삭제")
    public class DeleteRequestBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("도서 구매 요청 정보를 입력받아 삭제")
            public void deleteRequestBook() {
                // Given
                bookInfoRepo.save(bookInfo);
                memberRepo.save(member);

                RequestBook requestBook = RequestBook.builder()
                        .bookInfo(bookInfo)
                        .member(member)
                        .isApproved(false)
                        .build();
                RequestBook createdRequestBook = requestBookRepo.save(requestBook);

                // When
                requestBookRepo.delete(createdRequestBook);

                // Then
                assertEquals(0, requestBookRepo.findAll().size());
            }
        }
    }
}
