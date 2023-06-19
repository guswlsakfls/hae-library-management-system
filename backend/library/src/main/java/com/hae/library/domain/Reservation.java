package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userId", nullable = false)
    private int userId;

    @Column(name = "bookId", nullable = false)
    private int bookId;

    @Column(name = "status", nullable = false)
    private byte status;

    @Column(name = "endAt", nullable = false)
    private LocalDateTime endAt;
}
