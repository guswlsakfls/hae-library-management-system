package com.hae.library.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "CATEGORY")
@NoArgsConstructor
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
    public Category(String categoryName, Long id) {
        this.id = id;
        this.categoryName = categoryName;
    }

    /**
     * 카테고리 이름 변경
     * @param categoryName 변경할 카테고리 이름
     */
    public void updateCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void updateBookInfoList(ArrayList<Object> objects) {
        this.bookInfoList = bookInfoList;
    }
}
