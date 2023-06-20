package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lending")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lending extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lending_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lendingLibrarian_id")
    private Member lendingLibrarian;

    @Column(name = "lending_condition")
    private String lendingCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returning_id")
    private Member returningLibrarian;

    @Column(name = "returning_condition")
    private String returningCondition;

    @Column(name = "returning_at")
    private LocalDateTime returningAt;

    private boolean renew;
}
