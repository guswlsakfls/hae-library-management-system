package com.hae.library.domain.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BookStatus {
    BREAK("BOOK_BREAK"),
    FINE("BOOK_FINE"),
    LOST("BOOK_LOST");

    private final String type;

    public String getType() {
        return type;
    }
}
