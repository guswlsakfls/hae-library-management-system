package com.hae.library.repositoryTest;

import com.hae.library.domain.Enum.Role;
import com.hae.library.domain.Member;
import com.hae.library.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@DisplayName("MemberRepository 단위 테스트")
public class MemberRepositoryTest  {
    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원 생성")
    public class CreateMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("회원 정보를 입력받아 회원가입을 처리")
            public void CreateMemberSuccessTest() {
                // Given
                Member member = Member.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();

                // When
                Member createdMember = memberRepository.save(member);

                // Then
                assertNotNull(createdMember.getId());
                assertEquals(member.getEmail(), createdMember.getEmail());
                assertEquals(member.getPassword(), createdMember.getPassword());
                assertEquals(member.getRole(), createdMember.getRole());
            }
        }
        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("email 빈 값 대입시 생성하지 못한다")
            public void createMemberFailTest() {
                // Given
                Member member = Member.builder()
                        .email(null)
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();

                // When
                assertThrows(Exception.class, () -> memberRepository.save(member));
            }
        }
    }

    @Nested
    @DisplayName("회원 조회")
    public class ReadMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("모든 회원 정보를 조회한다")
            public void getAllMember() {
                // Given
                Member member1 = Member.builder()
                        .email("test1@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();

                Member member2 = Member.builder()
                        .email("test2@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_USER"))
                        .build();

                memberRepository.save(member1);
                memberRepository.save(member2);

                // When
                List<Member> members = memberRepository.findAll();

                // Then
                assertNotNull(members);
                assertEquals(3, members.size());
                assertTrue(members.stream()
                        .anyMatch(m -> m.getEmail().equals("test1@gmail.com") && m.getRole().equals(Role.valueOf("ROLE_ADMIN"))));
                assertTrue(members.stream()
                        .anyMatch(m -> m.getEmail().equals("test2@gmail.com") && m.getRole().equals(Role.valueOf("ROLE_USER"))));
            }


            @Test
            @DisplayName("email을 입력받아 회원 정보를 조회한다")
            public void ReadMemberSuccessTest() {
                // Given
                Member member = Member.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();
                Member createdMember = memberRepository.save(member);

                // When
                Member readMember = memberRepository.findByEmail("test@gmail.com").orElse(null);

                // Then
                assertNotNull(readMember);
                assertEquals(createdMember.getEmail(), readMember.getEmail());
            }
        }


        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {

            @Test
            @DisplayName("email이 null일 때 회원을 조회하지 못한다")
            public void readMemberFailTestNullEmail() {
                // Given
                Member member = Member.builder()
                        .email("admin@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();
                Member createdMember = memberRepository.save(member);

                // When
                Member readMember = memberRepository.findByEmail(null).orElse(null);

                // Then
                assertNull(readMember);
            }

            @Test
            @DisplayName("존재하지 않는 email로 회원을 조회하면 결과가 없다")
            public void readMemberFailTestUnknownEmail() {
                // Given
                Member member = Member.builder()
                        .email("admin@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();
                Member createdMember = memberRepository.save(member);

                // When
                Member readMember = memberRepository.findByEmail("unknown@gmail.com").orElse(null);

                // Then
                assertNull(readMember);
            }
        }
    }

    @Nested
    @DisplayName("회원 수정")
    public class UpdateMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("회원 정보를 입력받아 회원 정보를 수정한다")
            public void updateMemberSuccessTest() {
                // Given
                Member member = Member.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();
                Member createdMember = memberRepository.save(member);

                // When
                createdMember.updatePassword("4321");
                createdMember.updateRole(Role.valueOf("ROLE_USER"));
                createdMember.updateActivated(true);
                createdMember.updatePenaltyEndDate(LocalDateTime.now().plusDays(1));
                Member updatedMember = memberRepository.save(createdMember);

                // Then
                assertEquals(createdMember.getId(), updatedMember.getId());
                assertEquals(createdMember.getEmail(), updatedMember.getEmail());
                assertEquals(createdMember.getPassword(), updatedMember.getPassword());
                assertEquals(createdMember.getRole(), updatedMember.getRole());
                assertEquals(createdMember.isActivated(), updatedMember.isActivated());
                assertEquals(createdMember.getPenaltyEndDate(), updatedMember.getPenaltyEndDate());
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
            @Test
            @DisplayName("email이 null일 때 회원을 수정하지 못한다")
            public void updateMemberFailTestNullEmail() {
                // Given
                Member member = Member.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();
                Member createdMember = memberRepository.save(member);

                // When
                Member readMember = memberRepository.findByEmail(null).orElse(null);

                // Then
                assertNull(readMember);
            }
        }
    }

    @Nested
    @DisplayName("회원 삭제")
    public class DeleteMemberTest {
        @Nested
        @DisplayName("성공 케이스")
        public class SuccessCaseTest {
            @Test
            @DisplayName("email을 입력받아 회원 정보를 삭제한다")
            public void deleteMemberSuccessTest() {
                // Given
                Member member = Member.builder()
                        .email("test@gmail.com")
                        .password("1234")
                        .role(Role.valueOf("ROLE_ADMIN"))
                        .build();
                Member createdMember = memberRepository.save(member);

                // When
                memberRepository.delete(createdMember);

                // Then
                assertNull(memberRepository.findByEmail("test@gmail.com").orElse(null));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        public class FailCaseTest {
//            @Test
//            @DisplayName("email이 null일 때 회원을 삭제하지 못한다")
//            public void deleteMemberFailTestNullEmail() {
//                // Given
//                Member member = Member.builder()
//                        .email("test@gmail.com")
//                        .password("1234")
//                        .role(Role.valueOf("ROLE_ADMIN"))
//                        .build();
//
//                // When
//                memberRepository.delete(member);
//
//                // Then
////                assertNull(memberRepository.findByEmail(null).orElse(null));
//            }
        }
    }
}
