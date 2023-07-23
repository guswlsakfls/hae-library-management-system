package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 도서 대출을 나타내는 도메인 입니다
@Entity
@Getter
@NoArgsConstructor()
@Table(name = "lending")
//@Table(name = "LENDING")
public class Lending extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lending_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lending_user", nullable = false)
    private Member lendingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lending_librarian", nullable = false)
    private Member lendingLibrarian;

    @Column(name = "lending_condition", nullable = false, length = 300 )
    private String lendingCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returning_librarian")
    private Member returningLibrarian;

    @Column(name = "returning_condition", length = 300)
    private String returningCondition;

    @Column(name = "returning_end_at")
    private LocalDateTime returningEndAt;

    @Builder
    public Lending(Long id, Member lendingUser ,Member lendingLibrarian, String lendingCondition,
                   Member returningLibrarian, String returningCondition,
                   LocalDateTime returningEndAt) {
        this.id = id;
        this.lendingUser = lendingUser;
        this.lendingLibrarian = lendingLibrarian;
        this.lendingCondition = lendingCondition;
        this.returningLibrarian = returningLibrarian;
        this.returningCondition = returningCondition;
        this.returningEndAt = returningEndAt;
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
        this.lendingUser = user;

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
        this.lendingUser = member;
    }

    /**
     * 대출의 도서 정보를 업데이트합니다.
     * @param book 업데이트할 도서 정보
     */
    public void updateBook(Book book) {
        this.book = book;
    }
}
