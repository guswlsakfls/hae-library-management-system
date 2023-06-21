package com.hae.library.repository;

import com.hae.library.domain.Lending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LendingRepository extends JpaRepository<Lending, Long> {
}
