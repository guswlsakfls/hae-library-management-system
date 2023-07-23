package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 책 정보를 나타내는 도메인입니다
@Entity
@Getter
@Table(name = "BOOK_INFO")
@NoArgsConstructor()
public class BookInfo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.REMOVE)
    private List<Book> bookList = new ArrayList<>();

    @Column(name = "title", nullable = false, length = 300)
    private String title;

    @Column(name = "isbn", nullable = false, length = 13)
    private String isbn;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "publisher", length = 100)
    private String publisher;

    @Column(name = "image", length = 200)
    private String image;

    @Column(name = "published_at", length = 20)
    private String publishedAt;

    @OneToOne(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    private RequestBook requestBook;

    @Builder
    public BookInfo(Long id, String isbn, String title, String author, String publisher,
                    String image, String publishedAt, Category category, List<Book> bookList) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.publishedAt = publishedAt;
        this.category = category;
    }

    /**
     * 도서 정보를 업데이트합니다.
     * @param title       새로운 도서 제목
     * @param isbn        새로운 도서 ISBN
     * @param author      새로운 도서 작가
     * @param publisher   새로운 도서 출판사
     * @param publishedAt 새로운 도서 출판일
     * @param image       새로운 도서 이미지
     */
    public void updateBookInfo(String title, String isbn, String author, String publisher,
                               String publishedAt, String image, Category category) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.publishedAt = publishedAt;
        this.image = image;
        this.category = category;
    }

    /**
     * 카테고리와 책 정보 간의 관계를 설정합니다.
     * @param category 카테고리
     */
    public void addCategory(Category category) {
        this.category = category;

        // category.getBookInfoList()가 null인 경우 처리
        if (category.getBookInfoList() == null) {
            category.updateBookInfoList(new ArrayList<>());
        }

        if(!category.getBookInfoList().contains(this)) {
            category.getBookInfoList().add(this);
        }
    }

    /**
     * 도서 제목을 업데이트합니다.
     * @param title 새로운 도서 제목
     */
    public void updateTitle(String title) {
        this.title = title;
    }

    /**
     * ID를 업데이트합니다. (ID가 존재하지 않을 때 사용)
     * @param nonExistingId 새로운 ID
     */
    public void updateId(Long nonExistingId) {
        this.id = nonExistingId;
    }

    /**
     * 도서 재고 수량을 얻습니다.
     * @return 도서 재고 수량
     */
    public int getStockQuantity() {
        return this.bookList.size();
    }

    /**
     * 구매 요청 도서를 업데이트 합니다.
     * @param requestBook 책 정보
     */
    public void updateRequestBook(RequestBook requestBook) {
        this.requestBook = requestBook;
    }
}



