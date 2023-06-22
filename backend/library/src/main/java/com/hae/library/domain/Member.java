package com.hae.library.domain;


import com.hae.library.domain.Enum.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "user")
    private List<Lending> LendingList = new ArrayList<Lending>();

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

    @Column(name = "member_name", length = 10, nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false, length = 30)
    private String password;

    @Column(name = "penalty_end_date")
    private LocalDateTime penaltyEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Builder
    public Member(String name, String email, String password, LocalDateTime penaltyEndDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.penaltyEndDate = penaltyEndDate;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updatePenaltyEndDate(LocalDateTime penaltyEndDate) {
        this.penaltyEndDate = penaltyEndDate;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }
}
