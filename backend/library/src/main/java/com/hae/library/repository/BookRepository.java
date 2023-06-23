package com.hae.library.repository;

import com.hae.library.domain.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"bookInfo"})
    List<Book> findAll();

    boolean existsByCallSign(String callSign);
}
