package com.hae.library.repository;

import com.hae.library.domain.Lending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LendingRepository extends JpaRepository<Lending, Long> {
//    Optional<Lending> findByUserEmail(String email);
}
