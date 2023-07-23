package com.hae.library.repository;

import com.hae.library.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

// 회원 레포지토리입니다
public interface MemberRepository extends JpaRepository<Member, Long>,
        JpaSpecificationExecutor<Member> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmailAndIdIsNot(String email, Long id);
}
