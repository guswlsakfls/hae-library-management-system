package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 책 구매 요청 정보를 저장하는 클래스입니다.
@Entity
@Getter
@Table(name = "REQUEST_BOOK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestBook extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_info")
    private BookInfo bookInfo;

    @Column(name = "is_approved")
    private boolean isApproved = false;

    @Column(name = "approved_date")
    private LocalDateTime approvedAt;

    @Builder
    public RequestBook(Member member, BookInfo bookInfo, boolean isApproved) {
        this.member = member;
        this.bookInfo = bookInfo;
        this.isApproved = isApproved;
    }

    /**
     * 책 요청 정보를 승인합니다.
     */
    public void approve() {
        this.isApproved = true;
    }

    /**
     * 매핑관계를 설정합니다.
     * @param member 책을 요청한 회원
     * @param bookInfo 요청한 책
     */
    public void addMapping(Member member, BookInfo bookInfo) {
        this.member = member;
        this.bookInfo = bookInfo;
        if (bookInfo.getRequestBook() != this) {
            bookInfo.updateRequestBook(this);
        }
    }

    /**
     * 승인일을 현재 시간으로 변경합니다.
     */
    public void updateApprovedAt() {
        this.approvedAt = LocalDateTime.now();
    }
}
