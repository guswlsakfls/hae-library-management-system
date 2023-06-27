package com.hae.library.domain;

import com.hae.library.domain.Enum.BookStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor()
@Table(name = "BOOK")
public class Book extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_INFO_ID")
    private BookInfo bookInfo;

    @OneToOne(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Lending lending;

    @Column(name = "CALL_SIGN", nullable = false, unique = true)
    private String callSign;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status = BookStatus.FINE;

    @Column(nullable = false, length = 10)
    private String donator;

    @Builder
    public Book(String callSign, BookStatus status, String donator) {
        this.callSign = callSign;
        this.status = status;
        this.donator = donator;
    }

    public void updateBook(String newCallSign, BookStatus newStatus, String newDonator) {
        this.callSign = newCallSign;
        this.status = newStatus;
        this.donator = newDonator;
    }

    public void addBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;

        // 무한루프 체크
        if (!bookInfo.getBookList().contains(this)) {
            bookInfo.getBookList().add(this);
        }
    }

    public void updateBookStatus(BookStatus status) {
        this.status = status;
    }

    public void updateCallSign(String callSign) {
        this.callSign = callSign;
    }

    public void updateIdTest(Long id) {
        this.id = id;
    }

    public void updateBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
}
