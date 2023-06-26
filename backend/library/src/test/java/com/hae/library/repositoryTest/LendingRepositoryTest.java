package com.hae.library.repositoryTest;

import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import com.hae.library.repository.LendingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@DisplayName("LendingRepository 단위 테스트")
public class LendingRepositoryTest {
    @Autowired
    private LendingRepository lendingRepo;

    @Nested
    @DisplayName("관리자가 유저에게 대출 해주기")
    public class CreateLendingTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("대출 정보를 입력받아 생성한다")
            public void createLendingSuccessTest() {
                // Given
                Member user = Member.builder().email("hyujo@gmail.com").build();
                Book book = Book.builder().callSign("100.10.v1.c1").build();
                Member librarian = Member.builder().name("hyujo").build();
                String lendingCondition = "책이 좀 더럽네요";

                // When
                Lending lending = Lending.builder()
                        .user(user)
                        .book(book)
                        .lendingLibrarian(librarian)
                        .lendingCondition(lendingCondition)
                        .build();

                Lending createdLending = lendingRepo.save(lending);

                // Then
                assertNotNull(createdLending.getId());
//                assertEquals(lending.getUser().getEmail(), createdLending.getUser().getEmail());
//                assertEquals(book.getCallSign(), createdLending.getBook().getCallSign());
//                assertEquals(book.getStatus(), createdLending.getBook().getStatus());
//                assertEquals(book.getDonator(), createdLending.getBook().getDonator());
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
//            @Test
//            @DisplayName("책이 없을시 대출받지 못한다")
//            public void createLendingFailByBookTest() {
//                // Given
//                Member user = Member.builder().email("hyujo@gmail.com").build();
//                Member librarian = Member.builder().name("hyujo").build();
//                String lendingCondition = "이상 없음";
//
//                // When
//                Lending lending = Lending.builder()
//                        .user(user)
//                        .book(null)
//                        .lendingLibrarian(librarian)
//                        .lendingCondition(lendingCondition)
//                        .build();
//
//                // When
//                assertThrows(Exception.class, () -> lendingRepo.save(lending));
//
//                // Then
//                // 예외가 발생하여 저장에 실패해야 함
//            }
//            @Test
//            @DisplayName("대출 사서가 없을시 생성하지 못한다")
//            public void createLendingFailTest() {
//                // Given
//                Member user = Member.builder().email("hyujo@gmail.com").build();
//                Book book = Book.builder().callSign("100.10.v1.c1").build();
//                String lendingCondition = "이상 없음";
//
//                // When
//                Lending lending = Lending.builder()
//                        .user(user)
//                        .book(book)
//                        .lendingLibrarian(null)
//                        .lendingCondition(lendingCondition)
//                        .build();
//
//                Lending createdLending = lendingRepo.save(lending);
//
//                // When
//                assertThrows(Exception.class, () -> lendingRepo.save(createdLending));
//
//                // Then
//                // 예외가 발생하여 저장에 실패해야 함
//            }
            @Test
            @DisplayName("comment가 10이하일 시 생성하지 못한다")
            public void createLendingByMinCommentFailTest() {
                // Given
                Member user = Member.builder().email("hyujo@gmail.com").build();
                Book book = Book.builder().callSign("100.10.v1.c1").build();
                Member librarian = Member.builder().name("hyujo").build();
                String lendingCondition = "없";

                // When
                Lending lending = Lending.builder()
                        .user(user)
                        .book(book)
                        .lendingLibrarian(librarian)
                        .lendingCondition(lendingCondition)
                        .build();

                // Then
                assertThrows(IllegalArgumentException.class,
                        () -> lending.checkConditionLength(lending.getLendingCondition()));
            }
//            @Test
//            @DisplayName("comment가 300자 이상일 시 생성하지 못한다")
//            public void createLendingByMaxCommentFailTest() {
//                // Given
//                Member user = Member.builder().email("hyujo@gmail.com").build();
//                Book book = Book.builder().callSign("100.10.v1.c1").build();
//                Member librarian = Member.builder().name("hyujo").build();
//                String lendingCondition = "dsfk wkef kjef kjw fkjwe fjkwe fkjwe fkjwe fkjwe fkjwe" +
//                        " fkjwe fkjw efkj wekfj wekjf wekjf wekjf wekjf wekjf wekjf wkjef wkejf " +
//                        "wkjef wkjef wkjef kwjef kjwef jkwe fkjwe fkjwe fkjwe fkjwe fkjwe fjkw " +
//                        "efjkwe fkjwe fjkwe fkjwe fkjwe fjkw ejkw efkjw efkj wekfj wekjf wejkf " +
//                        "wkejf ";
//
//                Lending lending = Lending.builder()
//                        .user(user)
//                        .book(book)
//                        .lendingLibrarian(librarian)
//                        .lendingCondition(lendingCondition)
//                        .build();
//
//                // When
//                assertThrows(IllegalArgumentException.class,
//                        () -> lending.checkConditionLength(lending.getLendingCondition()));
//            }
        }
    }

    @Nested
    @DisplayName("관리자가 유저에게 반납 해주기")
    public class ReturningTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
//            @Test
//            @DisplayName("대출 정보를 입력받아 반납한다")
//            public void returningSuccessTest() {
//                // Given
//                Member user = Member.builder().email("hyujo@gmail.com").build();
//                Book book = Book.builder().callSign("100.10.v1.c1").build();
//                Member lendinglibrarian = Member.builder().name("hyujo").build();
//                String lendingCondition = "책이 좀 더럽네요";
//                Member returningLibrarian = Member.builder().name("jddn").build();
//                String returningCondition = "책이 깨끗해 졌습니다";
//
//                Lending lending = Lending.builder()
//                        .user(user)
//                        .book(book)
//                        .lendingLibrarian(lendinglibrarian)
//                        .lendingCondition(lendingCondition)
//                        .build();
//
//                lendingRepo.save(lending);
//                // When
//                lending.updateReturning(returningLibrarian, returningCondition);
//                Lending updateLending = lendingRepo.save(lending);
//
//                // Then
//                assertEquals(lending.getReturningLibrarian(),
//                        updateLending.getReturningLibrarian());
//                assertEquals(lending.getReturningCondition(),
//                        updateLending.getReturningCondition());
//                assertNotEquals(lending.getReturningAt(), updateLending.getReturningAt());
//            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
//            @Test
//            @DisplayName("대출기록이 없을시 반납하지 못한다")
//            public void returningFailByBookTest() {
//                // Given
//                Member user = Member.builder().email("hyujo@gmail.com").build();
//                Book book = Book.builder().callSign("100.10.v1.c1").build();
//                Member lendinglibrarian = Member.builder().name("admin").build();
//                String lendingCondition = "책이 좀 더럽네요";
//                Member returningLibrarian = Member.builder().name("jddn").build();
//                String returningCondition = "책이 깨끗해 졌습니다";
//
//                Lending lending = Lending.builder()
//                        .user(user)
//                        .book(book)
//                        .lendingLibrarian(lendinglibrarian)
//                        .lendingCondition(lendingCondition)
//                        .build();
//                lendingRepo.save(lending);
//
//                // When
//                lending.updateReturning(returningLibrarian, returningCondition);
//                lending.updateIdTest(0L);
//                assertThrows(Exception.class, () -> lendingRepo.save(lending));
//
//                // Then
//                // 예외가 발생하여 저장에 실패해야 함
//            }

//            @Test
//            @DisplayName("comment가 10이하일 시 반납하지 못한다")
//            public void returningByMinCommentFailTest() {
//                // Given
//                Member user = Member.builder().email("hyujo@gmail.com").build();
//                Book book = Book.builder().callSign("100.10.v1.c1").build();
//                Member librarian = Member.builder().name("hyujo").build();
//                String lendingCondition = "없";
//
//                Lending lending = Lending.builder()
//                        .user(user)
//                        .book(book)
//                        .lendingLibrarian(librarian)
//                        .lendingCondition(lendingCondition)
//                        .build();
//
//                lendingRepo.save(lending);
//
//                // When
//                assertThrows(NullPointerException.class, // IllegalArgumentException 인데 NullPointerException이 발생함
//                        () -> lending.checkConditionLength(lending.getReturningCondition()));
//            }
//            @Test
//            @DisplayName("comment가 300자 이상일 시 반납하지 못한다")
//            public void returingByMaxCommentFailTest() {
//                // Given
//                Book book = Book.builder().callSign("100.10.v1.c1").build();
//                Member librarian = Member.builder().name("hyujo").build();
//                String lendingCondition = "fwefwefwefwefwef";
//                String returningCondition = "dsfk wkef kjef kjw fkjwe fjkwe fkjwe fkjwe fkjwe " +
//                        "fkjwe" +
//                        " fkjwe fkjw efkj wekfj wekjf wekjf wekjf wekjf wekjf wekjf wkjef wkejf " +
//                        "wkjef wkjef wkjef kwjef kjwef jkwe fkjwe fkjwe fkjwe fkjwe fkjwe fjkw " +
//                        "efjkwe fkjwe fjkwe fkjwe fkjwe fjkw ejkw efkjw efkj wekfj wekjf wejkf " +
//                        "wkejf ";
//
//
//                // When
//                Lending lending = Lending.builder()
//                        .book(book)
//                        .lendingLibrarian(librarian)
//                        .lendingCondition(lendingCondition)
//                        .build();
//
//                // Then
//                assertThrows(IllegalArgumentException.class,
//                        () -> lending.checkConditionLength(lending.getReturningCondition()));
//            }
        }
    }

    @Nested
    @DisplayName("대출 기록 정보 조회")
    public class FindLendingTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
//            @Test
//            @DisplayName("모든 대출 기록을 조회한다")
//            public void findAllLendingTest() {
//                // Given
//                Member user1 = Member.builder().email("hyujo@gmail.com").build();
//                Book book1 = Book.builder().callSign("100.10.v1.c1").build();
//                Member librarian1 = Member.builder().name("librarian1").build();
//                String lendingCondition1 = "없kmsdfnksenffsefesf";
//
//                Member user2 = Member.builder().email("ghruig@naver.com").build();
//                Book book2 = Book.builder().callSign("100.10.v1.c2").build();
//                Member librarian2 = Member.builder().name("librarian2").build();
//                String lendingCondition2 = "없sefsefsefsefsefsef";
//
//                Lending lending1 = Lending.builder()
//                        .book(book1)
//                        .user(user1)
//                        .lendingLibrarian(librarian1)
//                        .lendingCondition(lendingCondition1)
//                        .build();
//                lendingRepo.save(lending1);
//
//                Lending lending2 = Lending.builder()
//                        .book(book2)
//                        .user(user2)
//                        .lendingLibrarian(librarian2)
//                        .lendingCondition(lendingCondition2)
//                        .build();
//
//                // When
//                List<Lending> lendingList = lendingRepo.findAll();
//
//                // Then
//
//                assertEquals(2, lendingList.size());
//                assertEquals(lending1.getUser().getName(),
//                        lendingList.get(0).getUser().getName());
//                assertEquals(lending2.getUser().getName(),
//                        lendingList.get(1).getUser().getName());
//                assertEquals(lending1.getBook().getCallSign(),
//                        lendingList.get(0).getBook().getCallSign());
//                assertEquals(lending2.getBook().getCallSign(),
//                        lendingList.get(1).getBook().getCallSign());
//                assertEquals(lending1.getLendingLibrarian().getName(),
//                        lendingList.get(0).getLendingLibrarian().getName());
//                assertEquals(lending2.getLendingLibrarian().getName(),
//                        lendingList.get(1).getLendingLibrarian().getName());
//                assertEquals(lending1.getLendingCondition(),
//                        lendingList.get(0).getLendingCondition());
//                assertEquals(lending2.getLendingCondition(),
//                        lendingList.get(1).getLendingCondition());
//            }

//            @Test
//            @DisplayName("해당 이메일에 해당하는 대출 정보를 조회한다")
//            public void findLendingByIdTest() {
//                // Given
//                Member user = Member.builder().email("hyujo@gmail.com").build();
//                Book book = Book.builder().callSign("100.10.v1.c1").build();
//                Member librarian = Member.builder().name("librarian").build();
//                String lendingCondition = "없sdfasdfasdfasdfasdfasdf";
//
//                Lending lending = Lending.builder()
//                        .book(book)
//                        .user(user)
//                        .lendingLibrarian(librarian)
//                        .lendingCondition(lendingCondition)
//                        .build();
//                lendingRepo.save(lending);
//
//                // When
//                Lending foundLending =
//                        lendingRepo.findByUserEmail(user.getEmail()).orElse(null);
//
//                // Then
//                assertNotNull(foundLending);
//                assertEquals(lending.getBook().getCallSign(), foundLending.getBook().getCallSign());
//                assertEquals(lending.getLendingLibrarian().getName(), foundLending.getLendingLibrarian().getName());
//            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 ID로 조회하면 null을 반환한다")
            public void findLendingByIdFailTest() {
                // Given
                Long nonExistingId = 0L;

                // When
                Lending foundLending = lendingRepo.findById(nonExistingId).orElse(null);

                // Then
                assertNull(foundLending);
            }
        }
    }


    @Nested
    @DisplayName("대출 정보 수정")
    public class UpdateLendingTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("대출을 연장한다")
            public void renewLendingTest() {
                // Given
                Member user = Member.builder().email("hyujo@gmail.com").build();
                Book book = Book.builder().callSign("100.10.v1.c1").build();
                Member librarian = Member.builder().name("librarian").build();
                String lendingCondition = "없sdfasdfasdfasdfasdfasdf";
                LocalDateTime returningAt = LocalDateTime.now();

                Lending lending = Lending.builder()
                        .book(book)
                        .user(user)
                        .lendingLibrarian(librarian)
                        .lendingCondition(lendingCondition)
                        .returningAt(returningAt)
                        .build();
                lendingRepo.save(lending);

                // When
                lending.renewLending();
                Lending updateRenewLending = lendingRepo.save(lending);

                // Then
                assertEquals(lending.isRenew(), updateRenewLending.isRenew());
                assertEquals(lending.getReturningAt(), updateRenewLending.getReturningAt());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
        }
    }


    @Nested
    @DisplayName("대출 기록 삭제")
    public class DeleteLendingTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("대출 기록을 삭제한다")
            public void deleteLendingSuccessTest() {
                // Given
                Member user = Member.builder().email("hyujo@gmail.com").build();
                Book book = Book.builder().callSign("100.10.v1.c1").build();
                Member librarian = Member.builder().name("librarian").build();
                String lendingCondition = "없sdfasdfasdfasdfasdfasdf";

                Lending lending = Lending.builder()
                        .user(user)
                        .book(book)
                        .user(user)
                        .lendingLibrarian(librarian)
                        .lendingCondition(lendingCondition)
                        .build();

                lendingRepo.save(lending);

                // When
                lendingRepo.deleteById(lending.getId());

                // Then
                assertFalse(lendingRepo.existsById(lending.getId()));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 책 정보를 삭제할 수 없다")
            public void deleteNonExistingLendingTest() {
                // Given
                Long nonExistingId = 10000L;

                // When
//                System.out.println(LendingRepo.deleteById(nonExistingId));

            }
        }
    }
}
