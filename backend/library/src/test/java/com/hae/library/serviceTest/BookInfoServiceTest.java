package com.hae.library.serviceTest;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.dto.Book.RequestBookWithBookInfoDto;
import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoDto;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import com.hae.library.dto.Member.RequestSignupDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.BookErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.CategoryRepository;
import com.hae.library.service.BookInfoService;
import jakarta.persistence.criteria.Predicate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookInfoServiceTest {
    @Mock
    private BookInfoRepository bookInfoRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private BookInfoService bookInfoService;

    private RequestBookWithBookInfoDto requestBookWithBookInfoDto;

    @BeforeEach
    public void setup() {
         requestBookWithBookInfoDto = RequestBookWithBookInfoDto.builder()
                            .callSign("12345")
                            .status("FINE")
                            .donator("John Doe")
                            .title("Java Programming")
                            .author("John Smith")
                            .isbn("9781234567890")
                            .image("book-image.jpg")
                            .publisher("ABC Publishing")
                            .publishedAt("2022-01-01")
                            .categoryName("총류")
                            .build();
    }

    @Nested
    @DisplayName("책 정보 생성")
    public class CreateBookInfoTest {

        @Nested
        @DisplayName("성공 케이스")
        public class Success {
            @Test
            @DisplayName("책 정보를 입력받아 책 정보를 생성한다.")
            void createBookInfoSuccess() {
                // Given
                BookInfo bookInfo = BookInfo.builder()
                        .title(requestBookWithBookInfoDto.getTitle())
                        .author(requestBookWithBookInfoDto.getAuthor())
                        .isbn(requestBookWithBookInfoDto.getIsbn())
                        .image(requestBookWithBookInfoDto.getImage())
                        .publisher(requestBookWithBookInfoDto.getPublisher())
                        .publishedAt(requestBookWithBookInfoDto.getPublishedAt())
                        .build();

                RequestBookInfoDto requestBookInfoDto = RequestBookInfoDto.builder()
                        .title(requestBookWithBookInfoDto.getTitle())
                        .author(requestBookWithBookInfoDto.getAuthor())
                        .isbn(requestBookWithBookInfoDto.getIsbn())
                        .image(requestBookWithBookInfoDto.getImage())
                        .publisher(requestBookWithBookInfoDto.getPublisher())
                        .publishedAt(requestBookWithBookInfoDto.getPublishedAt())
                        .categoryName(requestBookWithBookInfoDto.getCategoryName())
                        .build();

                // 테스트를 위한 mock 카테고리를 만듭니다.
                Category mockCategory = Category.builder()
                        .categoryName(requestBookWithBookInfoDto.getCategoryName())
//                        .bookInfoList(new ArrayList<>())
                        .build();

                when(categoryRepo.findByCategoryName(requestBookWithBookInfoDto.getCategoryName())).thenReturn(Optional.of(mockCategory));
                when(bookInfoRepo.save(any(BookInfo.class))).thenReturn(bookInfo);

                // When
                BookInfo createdBookInfo =
                        bookInfoService.createBookInfo(requestBookInfoDto);

                // Then
                Assertions.assertThat(createdBookInfo).isNotNull();
                Assertions.assertThat(createdBookInfo.getTitle()).isEqualTo(requestBookWithBookInfoDto.getTitle());
            }

        }

        @Test
        @DisplayName("책 정보 생성 실패 - 카테고리 없음")
        void createBookInfoFail_NoCategory() {
            // Given
            // 카테고리가 없을 경우, Optional.empty()를 반환합니다.
            RequestBookInfoDto requestBookInfoDto = RequestBookInfoDto.builder()
                    .title(requestBookWithBookInfoDto.getTitle())
                    .author(requestBookWithBookInfoDto.getAuthor())
                    .isbn(requestBookWithBookInfoDto.getIsbn())
                    .image(requestBookWithBookInfoDto.getImage())
                    .publisher(requestBookWithBookInfoDto.getPublisher())
                    .publishedAt(requestBookWithBookInfoDto.getPublishedAt())
                    .build();

            when(categoryRepo.findByCategoryName(requestBookInfoDto.getCategoryName())).thenReturn(Optional.empty());

            // When
            Exception exception = assertThrows(RestApiException.class, () -> {
                bookInfoService.createBookInfo(requestBookInfoDto);
            });

            // Then
            // 출력되는 에러코드가 맞는지 확인합니다.
            Assertions.assertThat(((RestApiException) exception).getErrorCode().getMessage()).contains(BookErrorCode.CATEGORY_NOT_FOUND.getMessage());
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
                Category category = Category.builder()
                        .categoryName("전체")
                        .build();

                BookInfo bookInfo1 = BookInfo.builder()
                        .title("Java Programming")
                        .author("John Smith")
                        .isbn("9781234567890")
                        .image("book-image.jpg")
                        .publisher("ABC Publishing")
                        .publishedAt("2022-01-01")
                        .category(category)
                        .build();

                BookInfo bookInfo2 = BookInfo.builder()
                        .title("Python Programming")
                        .author("Jane Doe")
                        .isbn("9780987654321")
                        .image("book-image.jpg")
                        .publisher("DEF Publishing")
                        .publishedAt("2022-01-01")
                        .category(category)
                        .build();

                Page<BookInfo> pageOfBookInfo = new PageImpl<>(List.of(bookInfo1, bookInfo2));

                int page = 0;
                int size = 10;
                String searchKey = "";
                String categoryName = "전체";
                String sort = "최신도서";

                Sort.Direction direction = sort.equals("최신도서") ?  Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
                Specification<BookInfo> spec = (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    // Empty specification
                    return cb.and(predicates.toArray(new Predicate[0]));
                };

                // TODO: 통합 테스트로 Specification 테스트를 시도 해야함.
                // specification객체를 직접 제어하는 대신 ArugmentMatchers를 사용하여 일부 또는 모든 인수를 일치 시켯습니다.
                when(bookInfoRepo.findAll(ArgumentMatchers.<Specification<BookInfo>>any(), ArgumentMatchers.eq(pageable))).thenReturn(pageOfBookInfo);

                // When
                Page<ResponseBookInfoDto> bookInfoList = bookInfoService.getAllBookInfo(searchKey
                        , page, size, categoryName, sort);

                // Then
                Assertions.assertThat(bookInfoList).isNotNull();
                Assertions.assertThat(bookInfoList.getTotalElements()).isEqualTo(2);
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
                        .category(Category.builder().categoryName("총류").build())
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
            @DisplayName("책 정보가 한 개도 없을 경우")
            void getAllBookInfoEmptyTest() {
                // Given
                // 빈 페이지를 반환합니다.
                Page<BookInfo> expectedPage = Page.empty();
                // Pageable 객체를 생성합니다.
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
                // Specification 객체를 통해 빈 값이 예상되는 메서드를 호출합니다.
                when(bookInfoRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

                // 검색 키워드, 페이지, 사이즈, 카테고리, 정렬 방식을 지정합니다.
                String searchKey = "";
                int page = 0;
                int size = 10;
                String categoryName = "총류";
                String sort = "최신도서";

                // When
                Page<ResponseBookInfoDto> bookInfoList = bookInfoService.getAllBookInfo(searchKey
                        , page, size, categoryName, sort);

                // Then
                // 빈 페이지가 반환되는지 확인합니다.
                Assertions.assertThat(bookInfoList.getContent()).isEmpty();
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
