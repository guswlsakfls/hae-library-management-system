package com.hae.library.repository;

import com.hae.library.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

//    @Override // TODO: JpaSpecificationExecutor 추가해서 에러난다 나중에 확인
    @EntityGraph(attributePaths = {"bookInfo"})
    boolean existsByCallSign(String callSign);

    @Query("SELECT b FROM Book b WHERE b.bookInfo.title LIKE CONCAT('%',:search,'%')")
    @EntityGraph(attributePaths = {"lending"})
    Page<Book> findAll(@Param("search") String search, Pageable pageable);

    Optional<Book> findByCallSign(String callSign);
}
