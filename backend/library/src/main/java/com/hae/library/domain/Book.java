package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Table(name = "book")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
