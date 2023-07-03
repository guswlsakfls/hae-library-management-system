package com.hae.library.domain.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 책 상태를 나타내는 enum
@RequiredArgsConstructor
@Getter
public enum BookStatus {
    FINE("BOOK_FINE"), // 양호
    BREAK("BOOK_BREAK"), // 손상
    LOST("BOOK_LOST"); // 분실

    private final String type;
}
