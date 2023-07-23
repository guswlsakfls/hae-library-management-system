package com.hae.library.repository;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

// 책 레포지토리입니다
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    // 청구기호로 책이 존재하는지 확인하는 메서드입니다.
    @EntityGraph(attributePaths = {"bookInfo"})
    boolean existsByCallSign(String callSign);

    // 청구기호로 책을 찾는 메서드입니다.
    Optional<Book> findByCallSign(String callSign);

    // 자신을 제외한 동일한 청구기호를 가진 책이 있는지 확인하는 메서드입니다.
    boolean existsByCallSignAndIdIsNot(String callSign, Long id);

    // 몇 권의 도서가 있는지 확인하는 메서드입니다.
    int countByBookInfo(BookInfo bookInfo);
}
