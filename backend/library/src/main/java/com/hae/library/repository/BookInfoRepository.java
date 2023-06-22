package com.hae.library.repository;


import com.hae.library.domain.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {

    Optional<BookInfo> findById(Long bookInfoId); // 테스트 케이스 에러나서 작성했는데 필요 한가?
}
