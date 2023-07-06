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

public interface BookInfoRepository extends JpaRepository<BookInfo, Long>,
        JpaSpecificationExecutor<BookInfo> {
    Optional<BookInfo> findByIsbn(String isbn);
    List<BookInfo> findByTitleContaining(String search);

    List<BookInfo> findByCategory(Category category);
}
