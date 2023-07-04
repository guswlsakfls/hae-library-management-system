package com.hae.library.dto.Category.Response;

import com.hae.library.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
