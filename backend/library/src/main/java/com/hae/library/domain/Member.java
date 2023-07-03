package com.hae.library.domain;

import com.hae.library.domain.Enum.Role;
import com.hae.library.dto.Member.RequestChangeMemberInfoDto;
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
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Lending> LendingList = new ArrayList<Lending>();

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "penalty_end_date")
    private LocalDateTime penaltyEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Column(name = "activated")
    private boolean activated = true;

    @Builder
    public Member(String email, String password, Role role,
                  LocalDateTime penaltyEndDate) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.penaltyEndDate = penaltyEndDate;
    }

    /**
     * 회원 정보를 업데이트합니다.
     * @param requestChangeMemberInfoDto 업데이트할 회원 정보 DTO
     */
    public void updateMemberInfo(RequestChangeMemberInfoDto requestChangeMemberInfoDto) {
        this.email = requestChangeMemberInfoDto.getEmail();
        this.penaltyEndDate = requestChangeMemberInfoDto.getPenaltyEndDate();
        this.role = requestChangeMemberInfoDto.getRole();
        this.activated = requestChangeMemberInfoDto.getActivated(); // TODO: isActivated()에서 바꿨는데, 나중에 확인
    }

    /**
     * 비밀번호를 업데이트합니다.
     * @param password 새로운 비밀번호
     */
    public void updatePassword(String password) {
        this.password = password;
    }

    /**
     * 회원 권한을 업데이트합니다.
     * @param role 새로운 회원 권한
     */
    public void updateRole(Role role) {
        this.role = role;
    }

    /**
     * 연체 종료일을 업데이트합니다.
     * @param penaltyEndDate 새로운 연체 종료일
     */
    public void updatePenaltyEndDate(LocalDateTime penaltyEndDate) {
        this.penaltyEndDate = penaltyEndDate;
    }

    /**
     * 회원 활성화 여부를 업데이트합니다.
     * @param activated 새로운 활성화 여부
     */
    public void updateActivated(boolean activated) {
        this.activated = activated;
    }

    /**
     * 대출 정보를 추가합니다.
     * @param lending 추가할 대출 정보
     */
    public void addLending(Lending lending) {
        lending.updateMember(this);
        this.LendingList.add(lending);
    }
}
