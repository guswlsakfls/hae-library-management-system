package com.hae.library.domain;

import com.hae.library.domain.Enum.BookStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor()
@Table(name = "BOOK")
public class Book extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_INFO_ID")
    private BookInfo bookInfo;

    @OneToOne(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Lending lending;

    @Column(name = "CALL_SIGN", nullable = false, unique = true, length = 255) // 예시: 255자 제한
    private String callSign;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status = BookStatus.FINE;

    @Column(nullable = false, length = 100)
    private String donator;

    @Builder
    public Book(String callSign, BookStatus status, String donator) {
        this.callSign = callSign;
        this.status = status;
        this.donator = donator;
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
     * 대여 정보를 추가합니다.
     * @param lending 추가할 대여 정보
     */
    public void addLending(Lending lending) {
        this.lending = lending;
    }

    /**
     * 도서 상태를 업데이트합니다.
     * @param status 새로운 도서 상태
     */
    public void updateBookStatus(BookStatus status) {
        this.status = status;
    }

    /**
     * 청구 기호를 업데이트합니다.
     * @param callSign 새로운 청구 기호
     */
    public void updateCallSign(String callSign) {
        this.callSign = callSign;
    }

    /**
     * 테스트용으로 사용되는 ID를 업데이트합니다.
     * @param id 새로운 ID
     */
    public void updateIdTest(Long id) {
        this.id = id;
    }

    /**
     * 도서 정보를 업데이트합니다.
     * @param bookInfo 업데이트할 도서 정보
     */
    public void updateBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
}
