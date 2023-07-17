package com.hae.library.dto.Member.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestChangNameDto {
    private Long id;
    private String name;

}
