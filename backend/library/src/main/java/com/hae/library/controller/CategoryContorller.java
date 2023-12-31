package com.hae.library.controller;

import com.hae.library.dto.Category.Request.RequestCreateCategoryDto;
import com.hae.library.dto.Category.Request.RequestUpdateCategoryDto;
import com.hae.library.dto.Category.Response.ResponseCategoryDto;
import com.hae.library.dto.Common.ResponseResultDto;
import com.hae.library.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api")
@Validated
public class CategoryContorller {
    private final CategoryService categoryService;

    /**
     * 카테고리를 생성합니다.
     *
     * @param requestCreateCategoryDto
     * @return "카테고리 생성에 성공하였습니다" 메시지
     */
    @PostMapping(value = "/admin/category")
    public ResponseResultDto<Object> createCategory(@RequestBody @Valid RequestCreateCategoryDto requestCreateCategoryDto) {
        log.info("카테고리 생성 요청: [POST] /category");
        categoryService.createCategory(requestCreateCategoryDto);

        log.info("카테고리 생성에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(200)
                .message("카테고리 생성에 성공하였습니다")
                .build();
    }

    /**
     * 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록
     */
    @GetMapping(value = "/category/all")
    public ResponseResultDto<Object> getAllCategory() {
        log.info("카테고리 목록 조회 요청: [GET] /category/all");
        List<ResponseCategoryDto> responseCategoryDtoList = categoryService.getAllCategory();

        log.info("카테고리 목록 조회에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(200)
                .message("카테고리 목록 조회에 성공하였습니다")
                .data(responseCategoryDtoList)
                .build();
    }

    /**
     * 카테고리를 수정합니다.
     *
     * @param requestUpdateCategoryDto
     * @return "카테고리 수정에 성공하였습니다" 메시지
     */
    @PutMapping(value = "/admin/category")
    public ResponseResultDto<Object> updateCategory(@RequestBody @Valid RequestUpdateCategoryDto requestUpdateCategoryDto) {
        log.info("카테고리 수정 요청: [PUT] /category - {}, {}", requestUpdateCategoryDto.getCategoryId(), requestUpdateCategoryDto.getUpdatedCategoryName());
        categoryService.updateCategory(requestUpdateCategoryDto);

        log.info("카테고리 수정에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(200)
                .message("카테고리 수정에 성공하였습니다")
                .build();
    }

    /**
     * 카테고리를 삭제합니다.
     *
     * @param categoryId
     * @return "카테고리 삭제에 성공하였습니다" 메시지
     */
    @DeleteMapping(value = "/admin/category/{categoryId}")
    public ResponseResultDto<Object> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        log.info("카테고리 삭제 요청: [DELETE] /category - {}", categoryId);
        categoryService.deleteCategory(categoryId);

        log.info("카테고리 삭제에 성공하였습니다");
        return ResponseResultDto.builder()
                .statusCode(200)
                .message("카테고리 삭제에 성공하였습니다")
                .build();
    }
}


