package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_mark")
@NoArgsConstructor
public class BookMark extends BaseTimeEntity {
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
