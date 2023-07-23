package com.hae.library.dto.Category.Request;

import com.hae.library.domain.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 카테고리 수정에 대한 요청 DTO입니다
@Getter
@NoArgsConstructor
public class RequestUpdateCategoryDto {
    @NotNull(message = "카테고리 id는 필수입니다")
    private Long categoryId;

    @NotBlank(message = "수정 될 카테고리 이름은 필수입니다")
    @Size(max = 20, message = "카테고리 이름은 20자를 넘을 수 없습니다")
    private String updatedCategoryName;

    @Builder
    public RequestUpdateCategoryDto(Long categoryId, String updatedCategoryName) {
        this.categoryId = categoryId;
        this.updatedCategoryName = updatedCategoryName;
    }

    // toEntity 생성
    public Category toEntity() {
        return Category.builder()
                .categoryName(updatedCategoryName)
                .build();
    }
}
