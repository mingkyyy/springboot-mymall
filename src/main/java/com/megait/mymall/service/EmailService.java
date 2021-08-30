package com.megait.mymall.service;

import com.megait.mymall.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Transactional
    public void sendEmail(Member member) {
        // TOKEN 생성
        String token = UUID.randomUUID().toString();

        // TOKEN 을 DB 에 update
        member.setEmailCheckToken(token);

        // 이메일 날리기
        String url =
                "http://localhost:8080/email-check?email=" + member.getEmail()
                        + "&token=" + token;

        String title = "[My Mall] 회원 가입에 감사드립니다. 딱 한 가지 과정이 남았습니다!";
        String message = "다음 링크를 브라우저에 붙여넣어주세요. 링크 : " + url;
        String sender = "mymall-admin-noreply@mymall.com";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(member.getEmail());
        mailMessage.setSubject(title);
        mailMessage.setText(message);
        mailMessage.setFrom(sender);
        javaMailSender.send(mailMessage);
    }
}
