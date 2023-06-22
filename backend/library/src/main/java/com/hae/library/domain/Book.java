package com.hae.library.domain;

import com.hae.library.domain.Enum.BookStatus;
import com.hae.library.domain.Enum.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Getter
@Table(name = "book")
@NoArgsConstructor()
public class Book extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id")
    private BookInfo bookInfoId;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private Lending lending;

    @Column(name = "call_sign", nullable = false, unique = true)
    private String callSign;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status = BookStatus.FINE;

    @Column(nullable = false, length = 10)
    private String donator;

    @Builder
    public Book(BookInfo bookInfoId, String callSign, BookStatus status, String donator) {
        this.bookInfoId = bookInfoId;
        this.callSign = callSign;
        this.status = status;
        this.donator = donator;
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

    public void updateBook(String newCallSign, BookStatus newStatus, String newDonator) {
        this.callSign = newCallSign;
        this.status = newStatus;
        this.donator = newDonator;
    }
}
