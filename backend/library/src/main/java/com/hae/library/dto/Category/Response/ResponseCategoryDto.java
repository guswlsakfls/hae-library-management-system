package com.hae.library.dto.Category.Response;

import com.hae.library.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 카테고리 조회에 대한 응답 DTO입니다
@Getter
@NoArgsConstructor
public class ResponseCategoryDto {
    private Long id;
    private String categoryName;

    public static ResponseCategoryDto from(Category category) {
        ResponseCategoryDto dto = new ResponseCategoryDto();
        dto.id = category.getId();
        dto.categoryName = category.getCategoryName();
        return dto;
    }
}
