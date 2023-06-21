package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.BookInfo;
import com.hae.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepo;

    @Transactional
    public void createBook() {
        Book book = new Book().builder()
                        .isbn("isbn")
                        .callSign("callSign")
                        .status(1)
                        .donator("donator")
                        .build();
        bookRepo.save(book);
    }

    @Transactional
    public void getAllBook() {
        bookRepo.findAll();
    }

    @Transactional
    public Optional<Book> getBookById(Long id) {
        return bookRepo.findById(id);
    }

    public Book updateBookById(Long bookId, Book updatedBook) {
        // 도서 ID로 도서 업데이트 로직 구현
        Optional<Book> existingBook = bookRepo.findById(bookId);
        if (existingBook.isPresent()) {
            // 기존 도서 정보를 업데이트
//            Book bookToUpdate = existingBook.get();
//            bookToUpdate.setTitle(updatedBook.getTitle());
//            bookToUpdate.setAuthor(updatedBook.getAuthor());
//            // 필요한 다른 속성도 업데이트

            return bookRepo.save(existingBook.get());
        } else {
            // 도서가 존재하지 않는 경우 예외 처리
            // 또는 새로운 도서로 생성할지 여부 결정
            // 예: throw new NotFoundException("도서를 찾을 수 없습니다.");
            return null;
        }
    }

    public void deleteBookById(Long bookId) {
        // 도서 ID로 도서 삭제 로직 구현
        bookRepo.deleteById(bookId);
    }


}
