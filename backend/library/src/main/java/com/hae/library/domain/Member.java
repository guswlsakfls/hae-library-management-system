package com.hae.library.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<LoginInfo> loginInfoList = new ArrayList<LoginInfo>();

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservationList = new ArrayList<Reservation>();

    @OneToMany(mappedBy = "member")
    private List<RequestBook> requestBookList = new ArrayList<RequestBook>();

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new ArrayList<Review>();

    @OneToMany(mappedBy = "member")
    private List<BookMark> bookMarkList = new ArrayList<BookMark>();

    @Column(name = "member_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "penalty_end_date")
    private LocalDateTime penaltyEndDate;

    @Column(nullable = false)
    private int role;
}
