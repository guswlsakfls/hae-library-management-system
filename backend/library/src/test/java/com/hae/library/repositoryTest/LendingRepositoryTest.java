package com.hae.library.repositoryTest;

import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import com.hae.library.repository.BookRepository;
import com.hae.library.repository.LendingRepository;
import com.hae.library.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@DisplayName("LendingRepository 단위 테스트")
public class LendingRepositoryTest {
    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    private Lending lending;
    private Member lendingUser;
    private Member lendingLibrarian;
    private Book book;

    @BeforeEach
    public void setUp() {
        lendingUser = Member.builder()
                .email("hyujo@gmail.com")
                .password("1234")
                .role(Role.ROLE_USER)
                .build();
        lendingLibrarian = Member.builder()
                .email("admin@gmail.com")
                .password("1234")
                .role(Role.ROLE_ADMIN)
                .build();
        book = Book.builder()
                .callSign("100.10.v1.c1")
                .status(BookStatus.FINE)
                .lendingStatus(false)
                .build();
        String lendingCondition = "이상없음";
        lending = Lending.builder()
                .lendingUser(lendingUser)
                .lendingLibrarian(lendingLibrarian)
                .lendingCondition(lendingCondition)
                .build();
        lending.addBook(book);
    }

    @Nested
    @DisplayName("대출 생성")
    public class CreatedLendingTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("대출 시 대출 정보를 입력받아 생성")
            public void saveLendingTest() {
                //Given
                lendingUser = memberRepository.save(lendingUser);
                lendingLibrarian = memberRepository.save(lendingLibrarian);
                book = bookRepository.save(book);

                Lending newLending = Lending.builder()
                        .lendingUser(lendingUser)
                        .lendingLibrarian(lendingLibrarian)
                        .lendingCondition("default condition")
                        .build();
                newLending.addBook(book);

                //When
                Lending savedLending = lendingRepository.save(newLending);

                //Then
                assertNotNull(savedLending.getId());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("대출 시 대출 정보를 입력받지 않으면 실패")
            public void saveLendingTest() {
                //Given
                lendingUser = memberRepository.save(lendingUser);
                lendingLibrarian = memberRepository.save(lendingLibrarian);

                Lending newLending = Lending.builder()
                        .lendingUser(lendingUser)
                        .lendingLibrarian(lendingLibrarian)
                        .lendingCondition("default condition")
                        .build();
                newLending.addBook(book);

                //When & Then
                assertThrows(InvalidDataAccessApiUsageException.class,
                        () -> lendingRepository.save(newLending));
            }
        }
    }

    @Nested
    @DisplayName("대출 조회")
    public class FindLendingTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("대출 정보를 조회")
            public void findLendingTest() {
                //Given
                Lending savedLending = lendingRepository.save(lending);

                //When
                Optional<Lending> foundLending = lendingRepository.findById(savedLending.getId());

                //Then
                assertTrue(foundLending.isPresent());
                assertEquals(lending.getLendingUser().getEmail(),
                        foundLending.get().getLendingUser().getEmail());
                assertEquals(lending.getBook().getCallSign(), foundLending.get().getBook().getCallSign());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("대출 정보가 없으면 실패")
            public void findLendingTest() {
                //Given
                Long notExistId = 5L;

                //When
                Optional<Lending> foundLending = lendingRepository.findById(notExistId);

                //Then
                assertFalse(foundLending.isPresent());
            }
        }
    }

    @Nested
    @DisplayName("대출 삭제")
    public class DeleteLendingTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("대출 정보를 삭제")
            public void deleteLendingTest() {
                //Given
                lendingUser = memberRepository.save(lendingUser);
                lendingLibrarian = memberRepository.save(lendingLibrarian);
                book = bookRepository.save(book);

                Lending newLending = Lending.builder()
                        .lendingUser(lendingUser)
                        .lendingLibrarian(lendingLibrarian)
                        .lendingCondition("default condition")
                        .build();
                newLending.addBook(book);

                //When
                Lending savedLending = lendingRepository.save(newLending);

                //When
                lendingRepository.deleteById(savedLending.getId());

                Optional<Lending> deletedLending = lendingRepository.findById(savedLending.getId());

                //Then
                assertFalse(deletedLending.isPresent());
            }
        }
    }
}
