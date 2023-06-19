package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lending")
@NoArgsConstructor
public class Lending extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "book_id", nullable = false)
    private int bookId;

    private String title;

    @Column(name = "lending_librarian_id", nullable = false)
    private int lendingLibrarianId;

    @Column(name = "lending_condition")
    private String lendingCondition;

    @Column(name = "returning_librarian_id", nullable = false)
    private int returningLibrarianId;

    @Column(name = "returning_condition")
    private String returningCondition;

    @Column(name = "returning_at")
    private LocalDateTime returningAt;

    private boolean renew;
}
