package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Table(name = "book_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfo extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    private ArrayList<RequestBook> requestBookList = new ArrayList<RequestBook>();

    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    private ArrayList<BookMark> bookMarkList = new ArrayList<BookMark>();

    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    private ArrayList<Review> reviewList = new ArrayList<Review>();

    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL)
    private ArrayList<Reservation> reservationList = new ArrayList<Reservation>();

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private int author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "image")
    private String image;

    @Column(name = "published_at")
    private String publishedAt;

}
