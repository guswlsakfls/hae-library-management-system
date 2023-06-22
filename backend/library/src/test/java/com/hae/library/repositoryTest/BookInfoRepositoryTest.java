package com.hae.library.repositoryTest;

import com.hae.library.domain.BookInfo;
import com.hae.library.repository.BookInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@TestPropertySource("classpath:application-test.properties") // 필요 없는 듯
@DisplayName("BookInfoRepository 단위 테스트")
public class BookInfoRepositoryTest {
    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Nested
    @DisplayName("책 정보 생성")
    public class CreateBookInfoTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책 정보를 입력받아 생성한다")
            public void CreateBookInfoSuccessTest() {
                // Given
                BookInfo bookInfo = BookInfo.builder()
                        .isbn("9791168473690")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .build();

                // When
                BookInfo createdBookInfo = bookInfoRepository.save(bookInfo);

                // Then
                assertNotNull(createdBookInfo.getId());
                assertEquals(bookInfo.getIsbn(), createdBookInfo.getIsbn());
                assertEquals(bookInfo.getTitle(), createdBookInfo.getTitle());
                assertEquals(bookInfo.getAuthor(), createdBookInfo.getAuthor());
                assertEquals(bookInfo.getPublisher(), createdBookInfo.getPublisher());
                assertEquals(bookInfo.getImage(), createdBookInfo.getImage());
                assertEquals(bookInfo.getPublishedAt(), createdBookInfo.getPublishedAt());
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("isbn null 값 대입시 생성하지 못한다")
            public void testCreateBookInfoFail() {
                // Given
                BookInfo bookInfo = BookInfo.builder()
                        .isbn(null)
                        .title("파이썬의 정석")
                        .author("조현진")
                        .publisher("테스트 출판사")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .build();

                // When
                assertThrows(Exception.class, () -> bookInfoRepository.save(bookInfo));

                // Then
                // 예외가 발생하여 저장에 실패해야 함
            }
        }
    }

    @Nested
    @DisplayName("책 정보 조회")
    public class FindBookInfoTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책 정보를 ID로 조회한다")
            public void testFindBookInfoById() {
                // Given
                BookInfo bookInfo = BookInfo.builder()
                        .isbn("1234567890")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .build();
                bookInfoRepository.save(bookInfo);

                // When
                BookInfo foundBookInfo = bookInfoRepository.findById(bookInfo.getId()).orElse(null);

                // Then
                assertNotNull(foundBookInfo);
                assertEquals(bookInfo.getId(), foundBookInfo.getId());
                assertEquals(bookInfo.getIsbn(), foundBookInfo.getIsbn());
                assertEquals(bookInfo.getTitle(), foundBookInfo.getTitle());
                assertEquals(bookInfo.getAuthor(), foundBookInfo.getAuthor());
                assertEquals(bookInfo.getPublisher(), foundBookInfo.getPublisher());
                assertEquals(bookInfo.getImage(), foundBookInfo.getImage());
                assertEquals(bookInfo.getPublishedAt(), foundBookInfo.getPublishedAt());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 ID로 조회하면 null을 반환한다")
            public void testFindBookInfoByIdFail() {
                // Given
                Long nonExistingId = 0L;

                // When
                BookInfo foundBookInfo = bookInfoRepository.findById(nonExistingId).orElse(null);

                // Then
                assertNull(foundBookInfo);
            }
        }
    }


    @Nested
    @DisplayName("책 정보 수정")
    public class UpdateBookInfoTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책 정보를 수정한다")
            public void testUpdateBookInfo() {
                // Given
                BookInfo bookInfo = BookInfo.builder()
                        .isbn("1234567890")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .build();
                bookInfoRepository.save(bookInfo);

                // When
                String newTitle = "Updated Book";
                bookInfo.setTitle(newTitle);
                BookInfo updatedBookInfo = bookInfoRepository.save(bookInfo);

                // Then
                assertEquals(newTitle, updatedBookInfo.getTitle());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 책 정보는 수정할 수 없다")
            public void testUpdateNonExistingBookInfo() {
                // Given
                Long nonExistingId = 0L;

                // When
                BookInfo nonExistingBookInfo = new BookInfo();
                nonExistingBookInfo.setTitle("Updated Book");

                // Then
                assertThrows(Exception.class, () -> bookInfoRepository.save(nonExistingBookInfo));
            }
        }
    }


    @Nested
    @DisplayName("책 정보 삭제")
    public class DeleteBookInfoTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("책 정보를 삭제한다")
            public void testDeleteBookInfoSuccess() {
                // Given
                BookInfo bookInfo = BookInfo.builder()
                        .isbn("1234567890")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .build();
                bookInfoRepository.save(bookInfo);

                // When
                bookInfoRepository.deleteById(bookInfo.getId());

                // Then
                assertFalse(bookInfoRepository.existsById(bookInfo.getId()));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("존재하지 않는 책 정보를 삭제할 수 없다")
            public void testDeleteNonExistingBookInfo() {
                // Given
                Long nonExistingId = 10000L;

                // When
//                System.out.println(bookInfoRepository.deleteById(nonExistingId));

            }
        }
    }

}
