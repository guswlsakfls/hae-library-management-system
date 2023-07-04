package com.hae.library.dto.Category.Request;

import com.hae.library.domain.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestCreateCategoryDto {
    @NotBlank(message = "카테고리 이름은 필수 입력 값입니다")
    @Size(max = 20, message = "카테고리 이름은 20자 이내로 입력해주세요")
    private String categoryName;

    public Category toEntity() {
        return Category.builder()
                .categoryName(categoryName)
                .build();
    }
}
