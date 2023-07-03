package com.hae.library.repository;

import com.hae.library.domain.BookInfo;
import com.hae.library.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);
}
