package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "request_book")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userId", nullable = false)
    private int userId;

    @Column(name = "bookId", nullable = false)
    private int bookId;

}
