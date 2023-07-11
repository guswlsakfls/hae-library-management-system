package com.hae.library.repository;

import com.hae.library.domain.Book;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LendingRepository extends JpaRepository<Lending, Long>,
        JpaSpecificationExecutor<Lending> {
    // 도서 id로 대출정보를 가져옵니다.
//    Optional<Lending> findByBookId(Long bookId);

    // 도서를 대출 반납할 때, 반납사서가 null인 대출정보를 가져옵니다.(bookId로 조회하면 해당하는 모든 대출 정보를 가져오기 때문입니다.)
    Optional<Lending> findByBookIdAndReturningLibrarianIsNull(Long bookId);

//    Boolean existsByBookId(Long bookId);
//    List<Lending> findAllByUserId(Long userId);
//    List<Lending> findAllByLendingLibrarianId(Long lendingLibrarianId);
//    List<Lending> findAllByReturningLibrarianId(Long returningLibrarianId);
//
//    Page<Lending> findAllByUser(Specification<Lending> spec, Pageable pageable);


}
