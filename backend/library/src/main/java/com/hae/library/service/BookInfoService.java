package com.hae.library.service;

import com.hae.library.domain.BookInfo;
import com.hae.library.dto.BookInfo.RequestBookInfoDto;
import com.hae.library.repository.BookInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookInfoService {
    private final BookInfoRepository bookInfoRepo;

    @Transactional
    public boolean createBookInfo(RequestBookInfoDto requestBookInfoDto) {
        BookInfo bookInfo = new BookInfo().builder()
                .title(requestBookInfoDto.getTitle())
                .isbn(requestBookInfoDto.getIsbn())
                .author(requestBookInfoDto.getAuthor())
                .publisher(requestBookInfoDto.getPublisher())
                .image(requestBookInfoDto.getImage())
                .publishedAt(requestBookInfoDto.getPublishedAt())
                .build();
        Optional<BookInfo> optionalSavedBookInfo = Optional.ofNullable(bookInfoRepo.save(bookInfo));
        return optionalSavedBookInfo.isPresent();
    }

    @Transactional
    public List<BookInfo> getAllBookInfo() {
        return bookInfoRepo.findAll();
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
