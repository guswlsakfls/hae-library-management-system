package com.hae.library.domain.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 책 상태를 나타내는 enum
@RequiredArgsConstructor
@Getter
public enum BookStatus {
    FINE, BREAK, LOST
}
