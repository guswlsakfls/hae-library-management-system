package com.hae.library.service;

import com.hae.library.domain.BookInfo;
import com.hae.library.repository.BookInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BookInfoService {
    private final BookInfoRepository bookInfoRepo;

    @Transactional
    public void createBookInfo() {
        BookInfo bookInfo = new BookInfo().builder()
                .title("title")
                .isbn("isbn")
                .author("author")
                .publisher("publisher")
                .image("image")
                .publishedAt("publishedAt")
                .build();
        bookInfoRepo.save(bookInfo);
    }

    @Transactional
    public void getAllBookInfo() {
        bookInfoRepo.findAll();
    }

    @Transactional
    public void getBookInfoById(Long id) {
        bookInfoRepo.findById(id);
    }

    @Transactional
    public void updateBookInfoById() {
        BookInfo bookInfo = new BookInfo().builder()
                .title("title")
                .isbn("isbn")
                .author("author")
                .publisher("publisher")
                .image("image")
                .publishedAt("publishedAt")
                .build();
        bookInfoRepo.save(bookInfo);
    }

    @Transactional
    public void deleteBookInfoById(Long id) {
        bookInfoRepo.deleteById(id);
    }
}
