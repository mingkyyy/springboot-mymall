package com.megait.mymall.controller;

import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.MemberRepository;
import com.megait.mymall.service.MemberService;
import com.megait.mymall.util.CurrentMember;
import com.megait.mymall.validation.JoinFormValidator;
import com.megait.mymall.validation.JoinFormVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {


    private Logger log = LoggerFactory.getLogger(getClass());
    private final MemberService memberService;
    private final MemberRepository memberRepository;


    @InitBinder("joinFormVo") // 요청 전에 추가할 설정들 (Controller 에서 사용)
    protected void initBinder(WebDataBinder dataBinder){
        dataBinder.addValidators(new JoinFormValidator(memberRepository));
    }


    @RequestMapping("/")
    public String index(Principal principal, Model model){
        String message = "안녕하세요, 손님!";
        if(principal != null){
            message = "안녕하세요, " + principal.getName() + "님!";
        }
        model.addAttribute("msg", message);
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }




    @RequestMapping("/mypage/{email}")
    public String mypage(Model model,
                         @CurrentMember Member member, @PathVariable String email){

        // #this   ==> 이 객체 (자바의 this를 의미함)
        //              이 곳에서의 this는 로그인 중인 User형 객체. ==> 시큐리티의 User 객체를 의미.
        // member ==> this.getNam



        if(member == null || !member.getEmail().equals(email)) { //로그인을 안 한 상태
            return "rediredct:/";
        }
            model.addAttribute("member", member);
            return "member/mypage";

    }

    @GetMapping("/signup")
    public String signupForm(Model model){
        model.addAttribute("joinFormVo", new JoinFormVo());
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@Valid JoinFormVo vo, Errors errors){
        if(errors.hasErrors()){
            log.info("회원가입 에러 : {}", errors.getAllErrors());
            return "member/signup";
        }

        log.info("회원가입 정상!");

        memberService.processNewMember(vo);

        return "redirect:/"; // "/" 로 리다이렉트
    }

}
