package com.hae.library.repositoryTest;

import com.hae.library.domain.Category;
import com.hae.library.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("CategoryRepository 단위 테스트")
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepo;

    @Nested
    @DisplayName("카테고리 생성")
    public class CreateCategoryTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("카테고리 이름을 입력받아 생성")
            public void createCategory() {
                // Given
                Category categoryName = Category.builder()
                        .categoryName("소설")
                        .build();

                // When
                Category createdCategory = categoryRepo.save(categoryName);

                // Then
                assertNotNull(createdCategory.getId());
                assertEquals(categoryName.getCategoryName(), createdCategory.getCategoryName());
            }
        }
    }

    @Nested
    @DisplayName("카테고리 조회")
    public class ReadCategoryTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("카테고리 이름을 입력받아 조회")
            public void readCategory() {
                // Given
                Category categoryName = Category.builder()
                        .categoryName("소설")
                        .build();

                // When
                Category createdCategory = categoryRepo.save(categoryName);

                // Then
                assertNotNull(createdCategory.getId());
                assertEquals(categoryName.getCategoryName(), createdCategory.getCategoryName());
            }

            @Test
            @DisplayName("카테고리 이름을 입력받아 존재하는지 확인")
            public void testExistsByCategoryName() {
                // Given
                String categoryName = "테스트 카테고리";
                Category category = Category.builder()
                        .categoryName(categoryName)
                        .bookInfoList(new ArrayList<>())
                        .build();
                categoryRepo.save(category);

                // When
                boolean exists = categoryRepo.existsByCategoryName(categoryName);

                // Then
                assertTrue(exists);
            }
        }
    }

    @Nested
    @DisplayName("카테고리 수정")
    public class UpdateCategoryTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("카테고리 이름을 입력받아 수정")
            public void updateCategory() {
                // Given
                String updateCategoryName = "수정된 카테고리";
                Category categoryName = Category.builder()
                        .categoryName("소설")
                        .build();
                Category createdCategory = categoryRepo.save(categoryName);

                // When
                createdCategory.updateCategoryName(updateCategoryName);

                // Then
                assertEquals(updateCategoryName , createdCategory.getCategoryName());
            }
        }
    }

    @Nested
    @DisplayName("카테고리 삭제")
    public class DeleteCategoryTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("카테고리 이름을 입력받아 삭제")
            public void deleteCategory() {
                // Given
                String categoryName = "소설";
                Category category = Category.builder()
                        .categoryName(categoryName)
                        .build();
                Category createdCategory = categoryRepo.save(category);

                // When
                categoryRepo.delete(createdCategory);

                // Then
                assertFalse(categoryRepo.existsByCategoryName(categoryName));
            }
        }
    }
}
