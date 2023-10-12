package com.restaurantProject.famousrestaurant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RegisterMail{

    private final JavaMailSender emailsender;

    private String ePw; // 인증번호

    // 메일 내용 작성
    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailsender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);// 보내는 대상
        message.setSubject("임시 비밀번호 발급");// 제목

        /* 본문 내용 html */
        String msgg = "";
        msgg += "<div style='background-color: #f2f2f2; padding: 20px;'>";
        msgg += "<div style='max-width: 400px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; background-color: #fff;'>";
        msgg += "<img src='https://oopy.lazyrockets.com/api/v2/notion/image?src=https%3A%2F%2Fthumbnail7.coupangcdn.com%2Fthumbnails%2Fremote%2F492x492ex%2Fimage%2Frs_quotation_api%2Fysrimegn%2F61c98841c46b4834becfb17ae6097027.jpg&blockId=2618ba81-a66c-4218-aff7-0e1ca5ba2b51' style='width: 100px; display: block; margin: 0 auto;'>";
        msgg += "<h1 style='text-align: center; color: #007bff; font-family: Arial, sans-serif;'>안녕하세요</h1>";
        msgg += "<div style='text-align: center;'>";
        msgg += "<h3 style='color: #007bff;'>임시 비밀번호 입니다.</h3>";
        msgg += "<div style='font-size: 24px; font-weight: bold; color: #333;'>" + ePw + "</div>";
        msgg += "</div>";
        msgg += "</div>";
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        message.setFrom(new InternetAddress("tjdgur981@naver.com", "tjdgur"));// 보내는 사람

        return message;
    }

    // 랜덤 인증 코드 전송
    public String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨

            switch (index) {
                case 0:
                    key.append((char)(rnd.nextInt(26) + 97));
                    // a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char)(rnd.nextInt(26)) + 65);
                    // A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담고 bean 으로 등록해둔 javaMail 객체를 사용해서 이메일 send
    public String sendSimpleMessage(String to) throws Exception {
        ePw = createKey(); // 랜덤 인증번호 생성
        MimeMessage message = createMessage(to); // 메일 발송
        try {// 예외처리
            emailsender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }
}