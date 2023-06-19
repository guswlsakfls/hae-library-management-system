package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@NoArgsConstructor
public class Book extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String isbn;

    @Column(name = "call_sign")
    private String callSign;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private String donator;
}
