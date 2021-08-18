package com.megait.mymall.configuration;

//시큐리티 관련 configuration 빈

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 웹 관련 보안 설정 (예. 방화벽)
    @Override
    public void configure(WebSecurity web) throws Exception {
        //정적리소스 파일 (예를 들면 사진!!) 커밋 올
        web.ignoring() //시큐리티 필터링 자체를 건너 뛴다.
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }

    // 요청/응답 관련 보안 설정 (web 보다는 범위가 광범위 하다.)
    @Override
    protected void configure(HttpSecurity http) throws Exception {

    /*    // "/"이 요청은 모든 사용자에게 허용 시큐리티로 아이디 비밀번호 안 치고 들어와도 됨!
        http.authorizeRequests().mvcMatchers("/").permitAll();

        http.formLogin() //로그인 form 사용 및 설정 하겠다.
                .loginPage("/login")  //로그인 페이지 경로가 "/login"가 아닌 다른 경로로 잡고 싶을때 사용
                .defaultSuccessUrl("/",true);
        //로그인 성공했을때 리다이렉트 할 경로 (alwaysUse : 매번 사용을 하겠다 써도 되고 안 써도 됨!)

        http.logout() //로그아웃 관련 설정
        .logoutUrl("/logout") //로그아웃을 실행할 url. 기본 : "/logout" 다른 경로 하고 싶은 다른 경로로 씀
        .invalidateHttpSession(true) //로그아웃 완료 시 세션을 종료시킬 것인가.
        .logoutSuccessUrl("/"); //로그아웃 성공시 리다이렉트 할 경로로*/


        //객체 체이닝 방법을 사용해서 하나의 프로세스 형태로 진행 한다면?

        //mvcMatchers() : MVC 컨트롤러가 사용하는 요청 URL 패턴.
        //antMatchers() : 표현 방식이 ant 방식을 쓴다. **사용 할 수 있는 URL 패턴.
        // *-> 0개 이상의 문자와 매칭 **->0개 이상의 파일,디렉토리 매칭

        http
                .authorizeRequests()
                    .mvcMatchers("/")
                    .permitAll()

                     //주소가 mypage ~~ 이렇게 되는 놈들은 무조건 로그인 해야함!!
                    .antMatchers("/mypage/**")
                    .authenticated()
        .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/",true)
        .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/")
        ;
    }
}
