package com.hae.library.serviceTest;

import com.hae.library.domain.BookInfo;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.service.BookInfoService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookInfoServiceTest {

    @Mock
    private BookInfoRepository bookInfoRepo;

    @InjectMocks
    private BookInfoService bookInfoService;

    private RequestBookWithBookInfoDto createRequestBookInfoWithBook() {
        RequestBookWithBookInfoDto requestBookWithBookInfoDto =
                RequestBookWithBookInfoDto.builder()
                        .callSign("12345")
                        .status("FINE")
                        .donator("John Doe")
                        .title("Java Programming")
                        .author("John Smith")
                        .isbn("9781234567890")
                        .image("book-image.jpg")
                        .publisher("ABC Publishing")
                        .publishedAt("2022-01-01")
                        .build();

        return requestBookWithBookInfoDto;
    }

    @Nested
    @DisplayName("책 정보 생성")
    public class CreateBookInfoTest {

        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("책 정보 생성")
            void createBookInfoSuccess() {
                // Given
                RequestBookWithBookInfoDto requestBookWithBookInfoDto = createRequestBookInfoWithBook();

                BookInfo bookInfo = BookInfo.builder()
                        .title(requestBookWithBookInfoDto.getTitle())
                        .author(requestBookWithBookInfoDto.getAuthor())
                        .isbn(requestBookWithBookInfoDto.getIsbn())
                        .image(requestBookWithBookInfoDto.getImage())
                        .publisher(requestBookWithBookInfoDto.getPublisher())
                        .publishedAt(requestBookWithBookInfoDto.getPublishedAt())
                        .build();

                when(bookInfoRepo.save(any(BookInfo.class))).thenReturn(bookInfo);

                // When
                ResponseBookInfoDto createdBookInfo = bookInfoService.createBookInfo(requestBookWithBookInfoDto);

                // Then
                Assertions.assertThat(createdBookInfo).isNotNull();
                Assertions.assertThat(createdBookInfo.getTitle()).isEqualTo(requestBookWithBookInfoDto.getTitle());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("책 정보 생성")
            void createBookInfoFail() {
                // Given
                RequestBookWithBookInfoDto requestBookWithBookInfoDto = createRequestBookInfoWithBook();

                when(bookInfoRepo.save(any(BookInfo.class))).thenReturn(null);

                // When
                Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    bookInfoService.createBookInfo(requestBookWithBookInfoDto);
                });

                // Then
                Assertions.assertThat(exception.getMessage()).isEqualTo("bookInfo cannot be null");
            }

        }
    }

    @Nested
    @DisplayName("책 정보 조회")
    public class GetBookInfoTest {

        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("모든 책 정보 조회")
            void getAllBookInfoTest() {
                // Given
                BookInfo bookInfo1 = BookInfo.builder()
                        .title("Java Programming")
                        .author("John Smith")
                        .isbn("9781234567890")
                        .image("book-image.jpg")
                        .publisher("ABC Publishing")
                        .publishedAt("2022-01-01")
                        .build();

                BookInfo bookInfo2 = BookInfo.builder()
                        .title("Python Programming")
                        .author("Jane Doe")
                        .isbn("9780987654321")
                        .image("book-image.jpg")
                        .publisher("DEF Publishing")
                        .publishedAt("2022-01-01")
                        .build();

                when(bookInfoRepo.findAll()).thenReturn(List.of(bookInfo1, bookInfo2));

                int page = 0;
                int size = 10;
                String searchKey = "";

                // When
                Page<ResponseBookInfoDto> bookInfoList = bookInfoService.getAllBookInfo(searchKey
                        , page, size);

                // Then
                Assertions.assertThat(bookInfoList).isNotNull();
//                Assertions.assertThat(bookInfoList.size()).isEqualTo(2);
            }

            @Test
            @DisplayName("id로 책 정보 조회")
            void getBookInfoByIdTest() {
                // Given
                BookInfo bookInfo = BookInfo.builder()
                        .title("Java Programming")
                        .author("John Smith")
                        .isbn("9781234567890")
                        .image("book-image.jpg")
                        .publisher("ABC Publishing")
                        .publishedAt("2022-01-01")
                        .build();

                when(bookInfoRepo.findById(anyLong())).thenReturn(Optional.ofNullable(bookInfo));

                // When
                ResponseBookInfoWithBookDto bookInfoDto = bookInfoService.getBookInfoById(1L);

                // Then
                Assertions.assertThat(bookInfoDto).isNotNull();
                Assertions.assertThat(bookInfoDto.getTitle()).isEqualTo(bookInfo.getTitle());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("하나도 책 정보가 없을 때")
            void getAllBookInfoEmptyTest() {
                // Given
                when(bookInfoRepo.findAll()).thenReturn(List.of());

                int page = 0;
                int size = 10;
                String searchKey = "";

                // When
                Page<ResponseBookInfoDto> bookInfoList = bookInfoService.getAllBookInfo(searchKey
                        , page, size);

                // Then
                Assertions.assertThat(bookInfoList).isNotNull();
//                Assertions.assertThat(bookInfoList.size()).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayName("책 정보 삭제")
    public class DeleteBookInfoTest {

        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("id로 책 정보 삭제 성공")
            void deleteBookInfoById() {
                // Given
                BookInfo bookInfo = BookInfo.builder()
                        .title("Java Programming")
                        .author("John Smith")
                        .isbn("9781234567890")
                        .image("book-image.jpg")
                        .publisher("ABC Publishing")
                        .publishedAt("2022-01-01")
                        .build();

                when(bookInfoRepo.findById(anyLong())).thenReturn(Optional.ofNullable(bookInfo));

                // When
                bookInfoService.deleteBookInfoById(1L);

                // Then
                verify(bookInfoRepo, times(1)).deleteById(anyLong());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class Fail {
            @Test
            @DisplayName("책 정보가 없을 때")
            void deleteBookInfoFail() {
                // Given
                when(bookInfoRepo.findById(anyLong())).thenReturn(Optional.empty());

                // When
                RestApiException exception = assertThrows(RestApiException.class, () -> {
                    bookInfoService.deleteBookInfoById(1L);
                });

                // Then
                Assertions.assertThat(exception.getErrorCode()).isEqualTo(BookErrorCode.BAD_REQUEST_BOOK);
            }
        }

    }
}
