package com.hae.library.dto.Lending;

import com.hae.library.domain.Lending;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LendingDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long userId;
    private String userName;
    private Long lendingLibrarianId;
    private String lendingLibrarianName;
    private String lendingCondition;
    private Long returningLibrarianId;
    private String returningLibrarianName;
    private String returningCondition;
    private String returningEndAt;
    private boolean renew;

    public static LendingDto from(Lending lending) {
        return LendingDto.builder()
                // 이 부분에 Lending 엔티티의 필드들을 LendingDto에 매핑하는 로직이 들어가야 합니다.
                .build();
    }
}
