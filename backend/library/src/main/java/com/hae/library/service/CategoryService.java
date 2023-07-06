package com.hae.library.service;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import com.hae.library.dto.Category.Request.RequestCreateCategoryDto;
import com.hae.library.dto.Category.Request.RequestUpdateCategoryDto;
import com.hae.library.dto.Category.Response.ResponseCategoryDto;
import com.hae.library.global.Exception.RestApiException;
import com.hae.library.global.Exception.errorCode.CategoryErrorCode;
import com.hae.library.repository.BookInfoRepository;
import com.hae.library.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepo;
    private final BookInfoRepository bookInfoRepo;

    /**
     * 카테고리를 생성합니다.
     *
     * @param requestCreateCategoryDto 카테고리 정보 DTO
     */
    public void createCategory(RequestCreateCategoryDto requestCreateCategoryDto) {
        // 카테고리가 중복되면 예외를 발생시킵니다.
        if (categoryRepo.existsByCategoryName(requestCreateCategoryDto.getCategoryName())) {
            throw new RestApiException(CategoryErrorCode.DUPLICATE_CATEGORY);
        }

        // 새로운 카테고리를 저장합니다.
        categoryRepo.save(requestCreateCategoryDto.toEntity());
    }

    /**
     * 모든 카테고리를 조회합니다.
     *
     * @return ResponseCategoryListDto 카테고리 정보 DTO
     */
    public List<ResponseCategoryDto> getAllCategory() {
        // 모든 카테고리를 조회합니다.
        return categoryRepo.findAll().stream()
                .map(ResponseCategoryDto::from)
                .collect(Collectors.toList());

    }

    /**
     * 카테고리를 Id로 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return ResponseCategoryDto 카테고리 정보 DTO
     */
    public ResponseCategoryDto getCategoryById(Long categoryId) {
        // 카테고리를 조회합니다.
        return ResponseCategoryDto.from(categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RestApiException(CategoryErrorCode.BAD_REQUEST_CATEGORY)));
    }

    /**
     * 카테고리를 수정 합니다.
     *
     * @param categoryDto 카테고리 수정할 정보 DTO
     * @return ResponseCategoryDto 수정된 카테고리 정보 DTO
     */
    public ResponseCategoryDto updateCategory(RequestUpdateCategoryDto categoryDto) {
        // 카테고리를 조회합니다.
        Category category = categoryRepo.findById(categoryDto.getCategoryId())
                .orElseThrow(() -> new RestApiException(CategoryErrorCode.BAD_REQUEST_CATEGORY));

        // 카테고리를 수정합니다.
        category.updateCategoryName(categoryDto.getUpdatedCategoryName());

        // 카테고리를 수정합니다.
        return ResponseCategoryDto.from(categoryRepo.save(category));
    }

    /**
     * 카테고리를 삭제합니다.
     *
     * @param categoryId 카테고리 ID
     */
    public void deleteCategory(Long categoryId) {
        // 카테고리를 조회합니다.
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RestApiException(CategoryErrorCode.BAD_REQUEST_CATEGORY));

        // 해당 카테고리를 참조하는 도서가 있는지 확인합니다.
        List<BookInfo> bookInfoList = bookInfoRepo.findByCategory(category);
        if (!bookInfoList.isEmpty()) {
            // 참조하는 도서가 있으면 예외를 발생시킵니다.
            throw new RestApiException(CategoryErrorCode.CATEGORY_IN_USE);
        }

        // 참조하는 도서가 없으면 카테고리를 삭제합니다.
        categoryRepo.deleteById(categoryId);
    }

}
