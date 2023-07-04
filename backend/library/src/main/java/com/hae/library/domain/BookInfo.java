package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Table(name = "BOOK_INFO")
@NoArgsConstructor()
public class BookInfo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_INFO_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.REMOVE)
    private List<Book> bookList = new ArrayList<>();

    // TODO: 주석 처리된 부분은 관계 매핑을 위한 코드. 추후에 필요하면 주석 해제
    // @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    // private List<RequestBook> requestBookList = new ArrayList<>();

    // @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    // private List<BookMark> bookMarkList = new ArrayList<>();

    // @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    // private List<Review> reviewList = new ArrayList<>();

    // @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    // private List<Reservation> reservationList = new ArrayList<>();

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "author", nullable = false, length = 50)
    private String author;

    @Column(name = "publisher", length = 50)
    private String publisher;

    @Column(name = "image")
    private String image;

    @Column(name = "published_at")
    private String publishedAt;

    @Builder
    public BookInfo(Long id, String isbn, String title, String author, String publisher,
                    String image, String publishedAt) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.publishedAt = publishedAt;
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
                               String publishedAt, String image) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.publishedAt = publishedAt;
        this.image = image;
    }

    /**
     * 카테고리와 책 정보 간의 관계를 설정합니다.
     * @param category 카테고리
     */
    public void addCategory(Category category) {
        this.category = category;

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
}



