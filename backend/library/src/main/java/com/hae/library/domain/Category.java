package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @OneToMany(mappedBy = "category")
    private List<BookInfo> bookInfoList = new ArrayList<BookInfo>();

    @Column(name = "category_name")
    private String categoryName;

    @Builder
    public Category(String categoryName, List<BookInfo> bookInfoList, Long id) {
        this.id = id;
        this.bookInfoList = bookInfoList;
        this.categoryName = categoryName;
    }

    public void updateCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
