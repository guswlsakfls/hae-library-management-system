package com.hae.library.domain;

import com.hae.library.domain.Enum.BookStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// 실물 책에 대한 정보를 담고 있는 도메인입니다(청구기호로 구분)
@Entity
@Getter
@NoArgsConstructor()
@Table(name = "BOOK")
public class Book extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info")
    private BookInfo bookInfo;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Lending> lendingList = new ArrayList<>();

    @Column(name = "lending_status", nullable = false)
    private boolean lendingStatus = false;

    @Column(name = "call_sign", nullable = false, length = 255) // 예시: 255자 제한
    private String callSign;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status = BookStatus.FINE;

    @Column(length = 100)
    private String donator;

    @Builder
    public Book(Long id, String callSign, Boolean lendingStatus, BookStatus status, String donator,
                BookInfo bookInfo) {
        this.id = id;
        this.callSign = callSign;
        this.lendingStatus = lendingStatus || false;
        this.status = status;
        this.donator = donator;
        this.bookInfo = bookInfo;
    }

    /**
     * 도서 정보를 업데이트합니다.
     * @param newCallSign 새로운 청구 기호
     * @param newStatus 새로운 도서 상태
     * @param newDonator 새로운 기증자
     */
    public void updateBook(String newCallSign, BookStatus newStatus, String newDonator) {
        this.callSign = newCallSign;
        this.status = newStatus;
        this.donator = newDonator;
    }

    /**
     * 도서 정보에 새로운 도서 정보를 추가합니다.
     * @param bookInfo 추가할 도서 정보
     */
    public void addBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;

        // 무한루프 체크
        if (!bookInfo.getBookList().contains(this)) {
            bookInfo.getBookList().add(this);
        }
    }

    /**
     * 도서 상태를 업데이트합니다.
     * @param status 새로운 도서 상태
     */
    public void updateBookStatus(BookStatus status) {
        this.status = status;
    }

    /**
     * 도서를 대출처리 합니다.
     */
    public void updateLendingStatus() {
        this.lendingStatus = true;
    }

    /**
     * 도서를 반납처리 합니다.
     */
    public void updateReturningStatus() {
        this.lendingStatus = false;
    }
}
