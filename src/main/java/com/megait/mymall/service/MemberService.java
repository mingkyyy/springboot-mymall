package com.megait.mymall.service;

import com.megait.mymall.domain.Member;
import com.megait.mymall.domain.MemberType;
import com.megait.mymall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service // @Controller 와 @Repository 사이의 비지니스 로직 담당
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 기본 관리자 계정 생성
    @PostConstruct  // MemberService 객체 생성 시 무조건 실행할 메서드
    public void createAdminMember(){

        memberRepository.save(Member.builder()
                .email("admin@test.com")
                .password(passwordEncoder.encode("admin123"))
                //패스워드 인코드 해줌. 비밀번호 db에 보이지 않도록 암호화 하는것.
                .joinedAt(LocalDateTime.now())
                .memberType(MemberType.ROLE_ADMIN)
                .build()
        );
    }

    /**
     *
     * @param username 로그인 처리를 할 username(id)
     * @return UserDetails 로그인 처리를 해줄 유저의 정보
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepository.findByEmail(username);
        Member member = optional.orElseThrow(
                () -> new UsernameNotFoundException("미등록 계정")
        );


        UserDetails userDetails = new User(
                member.getEmail(),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getMemberType().name()))
                //set 으로 넣어도 됨!1 authority 는 ㄴㄴ 컬렉션 형으로 너어야 함!
        );
        return userDetails;
    }
}
