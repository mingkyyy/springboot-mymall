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
