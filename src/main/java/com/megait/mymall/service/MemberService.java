package com.megait.mymall.service;

import com.megait.mymall.domain.Address;
import com.megait.mymall.domain.Member;
import com.megait.mymall.domain.MemberType;
import com.megait.mymall.repository.MemberRepository;
import com.megait.mymall.util.MemberUser;
import com.megait.mymall.validation.JoinFormVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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

        return new MemberUser(member);
    }

    /*
      1. joinFormVo 객체를 Member DB에 저장
      2. 이메일 보내기 => 이메일 인증하기
      3. 로그인 처리해주기기
    */
    public void processNewMember(JoinFormVo vo) {
        Member member =  saveNewMember(vo);
        sendEmail(member);
        login(member);
    }

    private Member saveNewMember(JoinFormVo vo) {
        Member member = Member.builder()
                .email(vo.getEmail())
                .password(passwordEncoder.encode(vo.getPassword()))
                .joinedAt(LocalDateTime.now())
                .memberType(MemberType.ROLE_USER)
                .address(Address.builder()
                        .postcode(vo.getPostcode())
                        .baseAddress(vo.getBaseAddress())
                        .detailAddress(vo.getDetailAddress()).build())
                .build();
        return memberRepository.save(member);
    }

    private void sendEmail(Member member){

    }

    //회원가입 하자마자 자동로그인 해주기
    private void login(Member member){
        MemberUser user = new MemberUser(member);

        // 유저 정보를 담은 인증 토큰 생성
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user,
                        user.getMember().getPassword()
                        ,user.getAuthorities()
                );

        // 인증 토큰을 SecurityContext에 저장한다. <- 로그인 되었다.
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);

    }


}
