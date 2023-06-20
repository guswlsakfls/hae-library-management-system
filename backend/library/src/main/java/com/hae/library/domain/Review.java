package com.hae.library.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userId", nullable = false)
    private int userId;

    @Column(name = "bookId", nullable = false)
    private int bookId;

    @Column(name = "comment", length = 255)
    private String comment;
}
