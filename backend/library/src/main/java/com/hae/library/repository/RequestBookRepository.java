package com.hae.library.repository;

import com.hae.library.domain.RequestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// 도서 요청 레포지토리입니다
public interface RequestBookRepository extends JpaRepository<RequestBook, Long>,
        JpaSpecificationExecutor<RequestBook> {
}
