package com.hae.library.repository;


import com.hae.library.domain.BookInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
    BookInfo findByIsbn(String isbn);
}
