package com.hae.library.serviceTest;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.dto.Category.Request.RequestCreateCategoryDto;
import com.hae.library.dto.Category.Request.RequestUpdateCategoryDto;
import com.hae.library.dto.Category.Response.ResponseCategoryDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.CategoryErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.CategoryRepository;
import com.hae.library.service.CategoryService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService 테스트")
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private BookInfoRepository bookInfoRepo;

    @InjectMocks
    private CategoryService categoryService;

    private RequestCreateCategoryDto requestCreateCategoryDto;

    @BeforeEach
    void setUp() {
        requestCreateCategoryDto = RequestCreateCategoryDto.builder()
                .categoryName("categoryName")
                .build();
    }

    @Nested
    @DisplayName("카테고리 생성")
    public class CreateCategoryTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCase {
            @Test
            @DisplayName("카테고리 이름을 입력받아 카테고리를 생성")
            void createCategory() {
                // given
                RequestCreateCategoryDto requestDto = requestCreateCategoryDto;
                when(categoryRepo.existsByCategoryName(any(String.class))).thenReturn(false);

                // when
                categoryService.createCategory(requestDto);

                // Then
                verify(categoryRepo, times(1)).existsByCategoryName(any(String.class));
                verify(categoryRepo, times(1)).save(any(Category.class));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCase {
            @Test
            @DisplayName("카테고리 이름이 중복되면 예외를 발생")
            void createCategory() {
                // given
                RequestCreateCategoryDto requestDto = requestCreateCategoryDto;
                when(categoryRepo.existsByCategoryName(any(String.class))).thenReturn(true);

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                         () -> categoryService.createCategory(requestDto));

                // Then
                assertEquals(CategoryErrorCode.DUPLICATE_CATEGORY, ((RestApiException) exception).getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("카테고리 조회")
    public class GetCategoryTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCase {
            @Test
            @DisplayName("모든 카테고리를 조회")
            void getAllCategory() {
                // given
                List<Category> categoryList = new ArrayList<>();
                categoryList.add(Category.builder()
                        .categoryName("categoryName1")
                        .build());
                categoryList.add(Category.builder()
                        .categoryName("categoryName2")
                        .build());

                when(categoryRepo.findAll()).thenReturn(categoryList);

                // when
                List<ResponseCategoryDto> responseDtoList = categoryService.getAllCategory();

                // Then
                verify(categoryRepo, times(1)).findAll();
                assertEquals(categoryList.size(), responseDtoList.size());
            }

            @Test
            @DisplayName("모든 카테고리 조회시 카테고리가 없으면 빈 리스트를 반환")
            void getAllCategory_Empty() {
                // given
                List<Category> categoryList = new ArrayList<>();
                when(categoryRepo.findAll()).thenReturn(categoryList);

                // when
                List<ResponseCategoryDto> responseDtoList = categoryService.getAllCategory();

                // Then
                verify(categoryRepo, times(1)).findAll();
                assertEquals(categoryList.size(), responseDtoList.size());
            }

            @Test
            @DisplayName("카테고리 ID로 카테고리를 조회")
            void getCategoryById() {
                // Given
                Long categoryId = 1L;
                Category mockCategory = Category.builder()
                        .categoryName("categoryName")
                        .build();
                when(categoryRepo.findById(any(Long.class))).thenReturn(Optional.of(mockCategory));

                // When
                ResponseCategoryDto responseCategoryDto = categoryService.getCategoryById(1L);

                // Then
                verify(categoryRepo, times(1)).findById(any(Long.class));
                assertEquals(mockCategory.getCategoryName(), responseCategoryDto.getCategoryName());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCase {
            @Test
            @DisplayName("카테고리 ID가 존재하지 않으면 예외를 발생")
            void getCategoryById() {
                // given
                Long categoryId = 100L;
                when(categoryRepo.findById(any(Long.class))).thenReturn(Optional.empty());

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> categoryService.getCategoryById(categoryId));

                // Then
                assertEquals(CategoryErrorCode.BAD_REQUEST_CATEGORY, ((RestApiException) exception).getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("카테고리 수정")
    public class UpdateCategoryTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCase {
            @Test
            @DisplayName("카테고리 ID와 카테고리 이름을 입력받아 카테고리를 수정")
            void updateCategory() {
                // Given
                Long categoryId = 1L;
                RequestUpdateCategoryDto requestDto = RequestUpdateCategoryDto.builder()
                        .categoryId(categoryId)
                        .updatedCategoryName("updatedCategoryName")
                        .build();

                Category mockCategory = Category.builder()
                        .categoryName("categoryName")
                        .build();

                when(categoryRepo.findById(any(Long.class))).thenReturn(Optional.of(mockCategory));
                when(categoryRepo.save(any(Category.class))).thenReturn(mockCategory);

                // When
                categoryService.updateCategory(requestDto);

                // Then
                verify(categoryRepo, times(1)).findById(any(Long.class));
                verify(categoryRepo, times(1)).save(any(Category.class));
                Assertions.assertEquals(requestDto.getUpdatedCategoryName(), mockCategory.getCategoryName());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCase {
            @Test
            @DisplayName("카테고리 ID가 존재하지 않으면 예외를 발생")
            void updateCategory() {
                // Given
                Long categoryId = 1L;
                RequestUpdateCategoryDto requestDto = RequestUpdateCategoryDto.builder()
                        .categoryId(categoryId)
                        .updatedCategoryName("updatedCategoryName")
                        .build();

                Category mockCategory = Category.builder()
                        .categoryName("categoryName")
                        .build();

                when(categoryRepo.findById(any(Long.class))).thenReturn(Optional.empty());

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> categoryService.updateCategory(requestDto));

                // Then
                assertEquals(CategoryErrorCode.BAD_REQUEST_CATEGORY, ((RestApiException) exception).getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("카테고리 삭제")
    public class DeleteCategoryTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCase {
            @Test
            @DisplayName("카테고리 ID를 입력받아 카테고리를 삭제")
            void deleteCategory() {
                // Given
                Long categoryId = 1L;
                Category mockCategory = Category.builder()
                        .categoryName("categoryName")
                        .build();

                when(categoryRepo.findById(any(Long.class))).thenReturn(Optional.of(mockCategory));

                // When
                categoryService.deleteCategory(categoryId);

                // Then
                verify(categoryRepo, times(1)).findById(any(Long.class));
                verify(categoryRepo, times(1)).deleteById(any(Long.class));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCase {
            @Test
            @DisplayName("카테고리 ID가 존재하지 않으면 예외를 발생")
            void deleteCategory() {
                // Given
                Long categoryId = 1L;
                when(categoryRepo.findById(any(Long.class))).thenReturn(Optional.empty());

                // When
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> categoryService.deleteCategory(categoryId));

                // Then
                assertEquals(CategoryErrorCode.BAD_REQUEST_CATEGORY, ((RestApiException) exception).getErrorCode());
            }

            @Test
            @DisplayName("카테고리가 도서에 참조되고 있으면 예외를 발생")
            void deleteCategoryWhenCategoryInUse() {
                // given
                Long categoryId = 1L;
                Category category = Category.builder()
                        .categoryName("categoryName")
                        .build();
                List<BookInfo> bookInfos = Arrays.asList(
                        BookInfo.builder()
                                .category(category)
                                .build()
                );

                when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
                when(bookInfoRepo.findByCategory(category)).thenReturn(bookInfos);

                // when
                RestApiException exception = assertThrows(RestApiException.class,
                        () -> categoryService.deleteCategory(categoryId));

                // Then
                assertEquals(CategoryErrorCode.CATEGORY_IN_USE, ((RestApiException) exception).getErrorCode());
            }
        }
    }
}
