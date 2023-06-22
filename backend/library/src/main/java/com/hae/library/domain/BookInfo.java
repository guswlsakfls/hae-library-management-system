package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Table(name = "book_info")
@Getter
@NoArgsConstructor()
public class BookInfo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

//    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
//    private ArrayList<RequestBook> requestBookList = new ArrayList<RequestBook>();

//    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
//    private ArrayList<BookMark> bookMarkList = new ArrayList<BookMark>();

//    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
//    private ArrayList<Review> reviewList = new ArrayList<Review>();
//
//    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
//    private ArrayList<Reservation> reservationList = new ArrayList<Reservation>();

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "image")
    private String image;

    @Column(name = "published_at")
    private String publishedAt;

    @Builder
    public BookInfo(String isbn, String title, String author, String publisher, String image,
                    String publishedAt) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.image = image;
        this.publishedAt = publishedAt;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateId(Long nonExistingId) {
        this.id = nonExistingId;
    }

}
