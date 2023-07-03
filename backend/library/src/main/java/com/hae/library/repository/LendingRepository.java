package com.hae.library.repository;

import com.hae.library.domain.Book;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface LendingRepository extends JpaRepository<Lending, Long>,
        JpaSpecificationExecutor<Lending> {
    Optional<Lending> findByBookId(Long bookId);
    Boolean existsByBookId(Long bookId);
    List<Lending> findAllByUserId(Long userId);
    List<Lending> findAllByLendingLibrarianId(Long lendingLibrarianId);
    List<Lending> findAllByReturningLibrarianId(Long returningLibrarianId);

    List<Lending> findAllByUser(Member user);
}
