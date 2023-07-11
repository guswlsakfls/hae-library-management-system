package com.hae.library.repositoryTest;

import com.hae.library.domain.Book;
import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("BookRepository 단위 테스트")
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Nested
    @DisplayName("책 생성")
    public class CreateBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책 실물 정보를 입력받아 생성한다")
            public void CreateBookSuccessTest() {
                // Given
                Book book = Book.builder()
                        .callSign("800.23.v1.c1")
                        .status(BookStatus.FINE)
                        .donator("John Doe")
                        .build();

                // When
                Book createdBook = bookRepository.save(book);

                // Then
                assertNotNull(createdBook.getId());
                assertEquals(book.getCallSign(), createdBook.getCallSign());
                assertEquals(book.getStatus(), createdBook.getStatus());
                assertEquals(book.getDonator(), createdBook.getDonator());
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("callSign 빈 값 대입시 생성하지 못한다")
            public void createBookFailTest() {
                // Given
                Book book = Book.builder()
                        .callSign(null)
                        .status(BookStatus.FINE)
                        .donator("John Doe")
                        .build();

                // When
                assertThrows(Exception.class, () -> bookRepository.save(book));

                // Then
                // 예외가 발생하여 저장에 실패해야 함
            }
        }
    }

    @Nested
    @DisplayName("책 조회")
    public class FindBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책을 ID로 조회하고 반환한다")
            public void findBookByIdTest() {
                // Given
                Book book = Book.builder()
                        .callSign("800.23.v1.c1")
                        .status(BookStatus.FINE)
                        .donator("John Doe")
                        .build();
                bookRepository.save(book);

                // When
                Book foundBook = bookRepository.findById(book.getId()).orElse(null);

                // Then
                assertNotNull(foundBook);
                assertEquals(book.getId(), foundBook.getId());
                assertEquals(book.getCallSign(), foundBook.getCallSign());
                assertEquals(book.getStatus(), foundBook.getStatus());
                assertEquals(book.getDonator(), foundBook.getDonator());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 ID로 조회하면 null을 반환한다")
            public void findBookByIdFailTest() {
                // Given
                Long nonExistingId = 0L;

                // When
                Book foundBook = bookRepository.findById(nonExistingId).orElse(null);

                // Then
                assertNull(foundBook);
            }
        }
    }


    @Nested
    @DisplayName("책 수정")
    public class UpdateBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책을 수정한다")
            public void updateBookTest() {
                // Given
                Book book = Book.builder()
                        .callSign("800.23.v1.c1")
                        .status(BookStatus.FINE)
                        .donator("John Doe")
                        .build();
                bookRepository.save(book);

                // When
                String newCallSign = "100.22.v2.c2";
                BookStatus newStatus = BookStatus.BREAK;
                String newDonator = "Jane Doe";
                book.updateBook(newCallSign, newStatus, newDonator);
                Book updatedBook = bookRepository.save(book);

                // Then
                assertEquals(newCallSign, updatedBook.getCallSign());
                assertEquals(newStatus, updatedBook.getStatus());
                assertEquals(newDonator, updatedBook.getDonator());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
        }
    }


    @Nested
    @DisplayName("책 삭제")
    public class DeleteBookTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책을 삭제한다")
            public void deleteBookSuccessTest() {
                // Given
                Book book = Book.builder()
                        .callSign("800.23.v1.c1")
                        .status(BookStatus.FINE)
                        .donator("John Doe")
                        .build();
                bookRepository.save(book);

                // When
                bookRepository.deleteById(book.getId());

                // Then
                assertFalse(bookRepository.existsById(book.getId()));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 책 정보를 삭제할 수 없다")
            public void deleteNonExistingBookTest() {
                // Given
                Long nonExistingId = 10000L;

                // When
//                System.out.println(BookRepository.deleteById(nonExistingId));

            }
        }
    }
}
