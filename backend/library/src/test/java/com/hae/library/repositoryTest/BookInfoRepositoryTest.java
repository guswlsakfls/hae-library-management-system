package com.hae.library.repositoryTest;

import com.hae.library.domain.BookInfo;
import com.hae.library.repository.BookInfoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
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
            public void createBookInfoTest() {
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
                Assertions.assertThat(createdBookInfo).isNotNull();
                Assertions.assertThat(createdBookInfo.getId()).isNotNull();
                Assertions.assertThat(createdBookInfo.getIsbn()).isEqualTo(bookInfo.getIsbn());
                Assertions.assertThat(createdBookInfo.getTitle()).isEqualTo(bookInfo.getTitle());
                Assertions.assertThat(createdBookInfo.getAuthor()).isEqualTo(bookInfo.getAuthor());
                Assertions.assertThat(createdBookInfo.getPublisher()).isEqualTo(bookInfo.getPublisher());
                Assertions.assertThat(createdBookInfo.getImage()).isEqualTo(bookInfo.getImage());
                Assertions.assertThat(createdBookInfo.getPublishedAt()).isEqualTo(bookInfo.getPublishedAt());
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("isbn null 값 대입시 생성하지 못한다")
            public void createBookInfoTest() {
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
                Assertions.assertThatThrownBy(() -> bookInfoRepository.save(bookInfo))
                        .isInstanceOf(Exception.class);

                // Then
                // 예외가 발생하여 저장에 실패해야 함
            }
        }
    }

    @Nested
    @DisplayName("모든 책 정보 조회")
    public class FindAllBookInfoTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("모든 책 정보를 조회한다")
            public void findAllBookInfoTest() {
                // Given
                BookInfo bookInfo1 = BookInfo.builder()
                        .isbn("1234567890")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .build();
                bookInfoRepository.save(bookInfo1);

                BookInfo bookInfo2 = BookInfo.builder()
                        .isbn("1234517890")
                        .title("Test Book")
                        .author("John Doe")
                        .publisher("Test Publisher")
                        .image("test.jpg")
                        .publishedAt("2023-06-30")
                        .build();
                bookInfoRepository.save(bookInfo2);

                // When
                Iterable<BookInfo> foundBookInfoList = bookInfoRepository.findAll();

                // Then
                Assertions.assertThat(foundBookInfoList).isNotNull();
                Assertions.assertThat(foundBookInfoList.spliterator().getExactSizeIfKnown()).isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("저장된 책 정보가 없을 때 빈 리스트를 반환한다")
            public void findAllBookInfoFailTest() {
                // Given

                // When
                Iterable<BookInfo> foundBookInfoList = bookInfoRepository.findAll();

                // Then
                Assertions.assertThat(foundBookInfoList).isNotNull();
                Assertions.assertThat(foundBookInfoList.spliterator().getExactSizeIfKnown()).isEqualTo(0);
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
            public void FindBookInfoByIdTest() {
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
                Assertions.assertThat(foundBookInfo).isNotNull();
                Assertions.assertThat(foundBookInfo.getId()).isEqualTo(bookInfo.getId());
                Assertions.assertThat(foundBookInfo.getIsbn()).isEqualTo(bookInfo.getIsbn());
                Assertions.assertThat(foundBookInfo.getTitle()).isEqualTo(bookInfo.getTitle());
                Assertions.assertThat(foundBookInfo.getAuthor()).isEqualTo(bookInfo.getAuthor());
                Assertions.assertThat(foundBookInfo.getPublisher()).isEqualTo(bookInfo.getPublisher());
                Assertions.assertThat(foundBookInfo.getImage()).isEqualTo(bookInfo.getImage());
                Assertions.assertThat(foundBookInfo.getPublishedAt()).isEqualTo(bookInfo.getPublishedAt());
            }

            @Test
            @DisplayName("책 정보를 ISBN으로 조회한다")
            public void FindBookInfoByISBNTest() {
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
                BookInfo foundBookInfo =
                        bookInfoRepository.findByIsbn(bookInfo.getIsbn()).orElse(null);

                // Then
                Assertions.assertThat(foundBookInfo).isNotNull();
                Assertions.assertThat(foundBookInfo.getId()).isEqualTo(bookInfo.getId());
                Assertions.assertThat(foundBookInfo.getIsbn()).isEqualTo(bookInfo.getIsbn());
                Assertions.assertThat(foundBookInfo.getTitle()).isEqualTo(bookInfo.getTitle());
                Assertions.assertThat(foundBookInfo.getAuthor()).isEqualTo(bookInfo.getAuthor());
                Assertions.assertThat(foundBookInfo.getPublisher()).isEqualTo(bookInfo.getPublisher());
                Assertions.assertThat(foundBookInfo.getImage()).isEqualTo(bookInfo.getImage());
                Assertions.assertThat(foundBookInfo.getPublishedAt()).isEqualTo(bookInfo.getPublishedAt());
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
                Assertions.assertThat(foundBookInfo).isNull();
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
                String newIsbn = "09877654331";
                String newTitle = "Updated Book";
                String newAuthor = "Doen Dken";
                String newPublisher = "Updated Publisher";
                String newImage = "updated.jpg";
                String newPublishedAt = "2023-07-01";

                bookInfo.updateBookInfo(newIsbn, newTitle, newAuthor, newPublisher, newImage, newPublishedAt);
                BookInfo updatedBookInfo = bookInfoRepository.save(bookInfo);

                // Then
                Assertions.assertThat(updatedBookInfo).isNotNull();
                Assertions.assertThat(updatedBookInfo.getId()).isEqualTo(bookInfo.getId());
                Assertions.assertThat(updatedBookInfo.getIsbn()).isEqualTo(newIsbn);
                Assertions.assertThat(updatedBookInfo.getTitle()).isEqualTo(newTitle);
                Assertions.assertThat(updatedBookInfo.getAuthor()).isEqualTo(newAuthor);
                Assertions.assertThat(updatedBookInfo.getPublisher()).isEqualTo(newPublisher);
                Assertions.assertThat(updatedBookInfo.getImage()).isEqualTo(newImage);
                Assertions.assertThat(updatedBookInfo.getPublishedAt()).isEqualTo(newPublishedAt);
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
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
                Assertions.assertThat(bookInfoRepository.findById(bookInfo.getId()).orElse(null)).isNull();
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
