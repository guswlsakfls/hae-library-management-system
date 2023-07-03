package com.hae.library.domain.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 사용자 권한을 나타내는 enum
@RequiredArgsConstructor
@Getter

// USER: 일반 사용자, ADMIN: 관리자
public enum Role {
    USER, ADMIN
}
