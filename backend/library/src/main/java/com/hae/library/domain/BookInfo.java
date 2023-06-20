package com.hae.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book_info")
@NoArgsConstructor
public class BookInfo extends BaseTimeEntity{
    @Id
    private String isbn;

    @Column(name = "category_id")
    private int categoryId;

    private String title;

    private int author;

    private String publisher;

    private String image;

    @Column(name = "published_at")
    private String publishedAt;

}
