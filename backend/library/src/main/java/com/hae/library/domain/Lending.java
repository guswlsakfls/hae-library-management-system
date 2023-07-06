package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "LENDING")
@NoArgsConstructor()
public class Lending extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LENDING_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LENDING_LIBRARIAN")
    private Member lendingLibrarian;

    @Column(name = "LENDING_CONDITION", nullable = false, length = 300 )
    private String lendingCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RETURNING_LIBRARIAN")
    private Member returningLibrarian;

    @Column(name = "RETURNING_CONDITION")
    private String returningCondition;

    @Column(name = "RETURNING_AT")
    private LocalDateTime returningEndAt;

    @Column(name = "RENEW")
    private boolean renew = false;

    @Builder
    public Lending(Member user ,Member lendingLibrarian, String lendingCondition,
                   Member returningLibrarian, String returningCondition,
                   LocalDateTime returningEndAt, boolean renew) {
        this.user = user;
        this.lendingLibrarian = lendingLibrarian;
        this.lendingCondition = lendingCondition;
        this.returningLibrarian = returningLibrarian;
        this.returningCondition = returningCondition;
        this.returningEndAt = returningEndAt;
        this.renew = renew;
    }

    /**
     * ID를 업데이트합니다.
     * @param id 새로운 ID
     */
    public void updateIdTest(Long id) {
        this.id = id;
    }

    private void updateRenew(boolean renew) {
        this.renew = renew;
    }

    /**
     * 대출을 연장합니다.
     */
    public void renewLending() {
        this.returningEndAt = this.returningEndAt.plusDays(7);
        this.updateRenew(true);
    }

    /**
     * 대출 조건의 길이를 확인합니다.
     * @param condition 대출 조건
     * @throws IllegalArgumentException 대출 조건 길이가 유효하지 않을 경우 예외를 던집니다.
     */
    public void checkConditionLength(String condition) {
        final int MIN_LENDING_CONDITION_LENGTH = 4;
        final int MAX_LENDING_CONDITION_LENGTH = 300;

        if (condition.length() < MIN_LENDING_CONDITION_LENGTH || condition.length() > MAX_LENDING_CONDITION_LENGTH) {
            throw new IllegalArgumentException("lendingCondition은 최소 " + MIN_LENDING_CONDITION_LENGTH + ", 최대 " + MAX_LENDING_CONDITION_LENGTH + " 글자 이상이어야 합니다.");
        }
    }

    /**
     * 반납 정보를 업데이트합니다.
     * @param returninglibrarian 반납한 사서
     * @param returningCondition 반납 조건
     * @param returningEndAt 반납 일시
     */
    public void updateReturning(Member returninglibrarian, String returningCondition, LocalDateTime returningEndAt) {
        this.returningLibrarian = returninglibrarian;
        this.returningCondition = returningCondition;
        this.returningEndAt = returningEndAt;
    }

    // 관계 매핑

    /**
     * 대출에 도서를 추가합니다.
     * @param book 추가할 도서
     */
    public void addBook(Book book) {
        this.book = book;

        // 무한루프 체크
        if (!book.getLendingList().contains(this)) {
            book.getLendingList().add(this);
        }
    }

    /**
     * 대출에 사용자를 추가합니다.
     * @param user 추가할 사용자
     */
    public void addUser(Member user) {
        this.user = user;

        // 무한루프 체크
        if (!user.getLendingList().contains(this)) {
            user.getLendingList().add(this);
        }
    }

    /**
     * 대출의 사용자 정보를 업데이트합니다.
     * @param member 업데이트할 사용자 정보
     */
    public void updateMember(Member member) {
        this.user = member;
    }

    /**
     * 대출의 도서 정보를 업데이트합니다.
     * @param book 업데이트할 도서 정보
     */
    public void updateBook(Book book) {
        this.book = book;
    }
}
