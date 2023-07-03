package com.hae.library.service;

import com.hae.library.domain.Member;
import com.hae.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// Spring Security를 사용하여 사용자 인증에 필요한 사용자 정보를 가져오는 클래스입니다.
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    // UserDetailsService 인터페이스의 메서드를 오버라이드 합니다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 주어진 이메일을 이용해 사용자 정보를 찾습니다.
        // 사용자 정보가 존재한다면 UserDetails 객체를 생성합니다. 사용자 정보가 없다면 예외를 발생시킵니다.
        log.info("loadUserByUsername: {}", email);
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    // Member 객체를 받아 UserDetails 객체를 생성하는 메서드입니다.
    private UserDetails createUserDetails(Member member) {
        // 사용자의 권한 정보를 GrantedAuthority 객체로 생성합니다.
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());
        log.info("grantedAuthority: {}", grantedAuthority);

        // UserDetails 인터페이스를 구현하는 User 객체를 생성합니다. 이때 사용자의 이메일, 비밀번호, 권한 정보를 파라미터로 전달합니다.
        return new User(
                member.getEmail(),
                member.getPassword(),
                Collections.singleton(grantedAuthority)  // 권한 정보를 컬렉션에 담아 전달합니다.
        );
    }
}
