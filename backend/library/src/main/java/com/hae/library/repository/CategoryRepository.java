package com.hae.library.repository;

import com.hae.library.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 카테고리 레포지토리입니다
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCategoryName(String categoryName);

    Optional<Category> findByCategoryName(String categoryName);
}
