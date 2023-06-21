package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Getter
@Table(name = "book")
@NoArgsConstructor()
public class Book extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id")
    private BookInfo bookInfoId;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private Lending lending;

    @Column(nullable = false)
    private String isbn;

    @Column(name = "call_sign")
    private String callSign;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false, length = 10)
    private String donator;

    @Builder
    public Book(BookInfo bookInfoId, String isbn, String callSign, Integer status, String donator) {
        this.bookInfoId = bookInfoId;
        this.isbn = isbn;
        this.callSign = callSign;
        this.status = status;
        this.donator = donator;
    }
}
