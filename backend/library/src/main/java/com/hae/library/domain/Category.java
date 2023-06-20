package com.hae.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@NoArgsConstructor
public class Category {
    @Id
    private int id;

    @Column(name = "category_name")
    private String categoryName;
}
