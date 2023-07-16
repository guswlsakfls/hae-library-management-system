package com.hae.library.dto.BookInfo.Request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestIsbnDto {
    @Pattern(regexp = "^[0-9]{13}$", message = "ISBN은 9~13자의 숫자로 이루어져야 합니다.")
    private String isbn;
}
