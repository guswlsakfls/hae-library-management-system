package com.hae.library.dto.BookInfo;

import com.hae.library.domain.BookInfo;
import com.hae.library.dto.Book.ResponseBookDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ResponseBookInfoWithBookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String image;
    private String publisher;
    private String publishedAt;
    private String category;
    private String callSign;
    private List<ResponseBookDto> bookList;

    public static ResponseBookInfoWithBookDto from(BookInfo bookInfo) {
        List<ResponseBookDto> responseBookList = bookInfo.getBookList()
                .stream()
                .map(ResponseBookDto::from)
                .collect(Collectors.toList());

        return ResponseBookInfoWithBookDto.builder()
                .id(bookInfo.getId())
                .title(bookInfo.getTitle())
                .author(bookInfo.getAuthor())
                .isbn(bookInfo.getIsbn())
                .image(bookInfo.getImage())
                .publisher(bookInfo.getPublisher())
                .publishedAt(bookInfo.getPublishedAt())
                .bookList(responseBookList)
                .build();
    }
}
