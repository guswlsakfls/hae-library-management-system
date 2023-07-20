package com.hae.library.repository;


import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

// 책 정보 레포지토리입니다
public interface BookInfoRepository extends JpaRepository<BookInfo, Long>,
        JpaSpecificationExecutor<BookInfo> {
    Optional<BookInfo> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<BookInfo> findByCategory(Category category);
}
