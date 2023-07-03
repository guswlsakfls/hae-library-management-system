package com.hae.library.dto.Book;

import com.hae.library.domain.BookInfo;
import com.hae.library.dto.BookInfo.ResponseBookInfoWithBookDto;
import lombok.*;

import java.util.List;

// TODO: 필요없는 변수들 제거
// 국립중앙도서관 API에서 받아온 데이터를 담는 DTO 입니다.
@Getter
@NoArgsConstructor
@ToString
public class RequestBookApiDto {
    private int total;
    private String kwd;
    private int pageNum;
    private int pageSize;
    private String category;
    private String sort;
    private List<BookApiResultDto> result; // 결과 내용이 담길 리스트

    // 국립중앙도서관 API에서 받아온 데이터를 도서 추가를 위해 반환합니다.
    @Builder
    public RequestBookApiDto(int total, String kwd, int pageNum, int pageSize, String category, String sort, List<BookApiResultDto> result) {
        this.total = total;
        this.kwd = kwd;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.category = category;
        this.sort = sort;
        this.result = result;
    }

    // text/json으로 받아온 데이터를 반환값에 맞게 변환 합니다.
    public ResponseBookInfoWithBookDto toResponseBookInfoWithBookDto() {
        // result 리스트의 첫 번째 항목만 사용합니다.
        String isbn = this.result.get(0).getIsbn();
        String imageUrl = generateImageUrl(isbn); // 이미지 URL 생성 로직을 별도의 메소드로 분리
        return ResponseBookInfoWithBookDto.builder()
                .title(this.result.get(0).getTitleInfo())
                .author(this.result.get(0).getAuthorInfo())
                .isbn(isbn)
                .image(imageUrl)
                .publisher(this.result.get(0).getPubInfo())
                .publishedAt(this.result.get(0).getPubYearInfo())
                .category(this.result.get(0).getKdcName1s())
                .callSign(this.result.get(0).getCallNo())
                .build();
    }

    private String generateImageUrl(String isbn) {
        return String.format("https://image.kyobobook.co.kr/images/book/xlarge/%s/x%s.jpg",
                    isbn.substring(isbn.length() - 3), isbn);
    }

}
