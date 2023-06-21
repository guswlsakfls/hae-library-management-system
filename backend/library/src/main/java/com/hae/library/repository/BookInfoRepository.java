package com.hae.library.repository;


import com.hae.library.domain.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {

}
