package com.hae.library.dto.Lending.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestCallsignDto {
    @NotBlank(message = "청구기호를 입력해주세요.")
    @Size(max=20, message = "20자 이하로 입력해 주세요")
    private String callsign;

    @Builder
    public RequestCallsignDto(String callsign) {
        this.callsign = callsign;
    }
}
