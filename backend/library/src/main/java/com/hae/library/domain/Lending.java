package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "lending")
@NoArgsConstructor()
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

    @Builder
    public Lending(String title, Book book, Member lendingLibrarian, String lendingCondition, Member returningLibrarian, String returningCondition, LocalDateTime returningAt, boolean renew) {
        this.title = title;
        this.book = book;
        this.lendingLibrarian = lendingLibrarian;
        this.lendingCondition = lendingCondition;
        this.returningLibrarian = returningLibrarian;
        this.returningCondition = returningCondition;
        this.returningAt = returningAt;
        this.renew = renew;
    }

    private void setRenew(boolean renew) {
        this.renew = renew;
    }

    public void setReturning(Member member, String lendingCondition) {
        this.returningLibrarian = member;
        this.lendingCondition = lendingCondition;
    }

    public void setRenew() {
        this.returningAt = this.returningAt.plusDays(7);
        this.setRenew(true);
    }
}
