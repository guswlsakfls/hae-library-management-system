package com.hae.library.repository;

import com.hae.library.domain.RequestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequestBookRepository extends JpaRepository<RequestBook, Long>,
        JpaSpecificationExecutor<RequestBook> {
}
