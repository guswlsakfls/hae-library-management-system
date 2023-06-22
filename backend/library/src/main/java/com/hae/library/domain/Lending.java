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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lendingLibrarian_id")
    private Member lendingLibrarian;

    @Column(name = "lending_condition", nullable = false, length = 300 )
    private String lendingCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returning_id")
    private Member returningLibrarian;

    @Column(name = "returning_condition")
    private String returningCondition;

    @Column(name = "returning_at")
    private LocalDateTime returningAt;

    @Column(name = "renew")
    private boolean renew = false;

    @Builder
    public Lending(Book book, Member user ,Member lendingLibrarian, String lendingCondition,
                   Member returningLibrarian, String returningCondition, LocalDateTime returningAt, boolean renew) {
        this.user = user;
        this.book = book;
        this.lendingLibrarian = lendingLibrarian;
        this.lendingCondition = lendingCondition;
        this.returningLibrarian = returningLibrarian;
        this.returningCondition = returningCondition;
        this.returningAt = returningAt;
        this.renew = renew;
    }

    public void updateIdTest(Long id) {
        this.id = id;
    }

    private void setRenew(boolean renew) {
        this.renew = renew;
    }

    public void setReturning(Member member, String lendingCondition) {
        this.returningLibrarian = member;
        this.lendingCondition = lendingCondition;
    }

    public void renewLending() {
        this.returningAt = this.returningAt.plusDays(7);
        this.setRenew(true);
    }

    public void checkConditionLength(String condition) {
        final int MIN_LENDING_CONDITION_LENGTH = 4;
        final int MAX_LENDING_CONDITION_LENGTH = 300;

        if (condition.length() < MIN_LENDING_CONDITION_LENGTH || condition.length() > MAX_LENDING_CONDITION_LENGTH) {
            throw new IllegalArgumentException("lendingCondition은 최소 " + MIN_LENDING_CONDITION_LENGTH + ", 최대 " + MAX_LENDING_CONDITION_LENGTH + " 글자 이상이어야 합니다.");
        }
    }

    public void updateReturning(Member returninglibrarian, String returningCondition) {
        this.returningLibrarian = returninglibrarian;
        this.returningCondition = returningCondition;
        this.returningAt = LocalDateTime.now();
    }

}
