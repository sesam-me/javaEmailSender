package com.example.mailtest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    // 메일 내용 작성
    private final JavaMailSender emailSender;
    // 사용자가 메일로 받을 인증번호
    private String ePw;
    // 메일 내용 작성
    public MimeMessage creatMessage(String to) throws MessagingException, UnsupportedEncodingException {
        //System.out.println("메일받을 사용자" + to);
        //System.out.println("인증번호" + ePw);
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to); // 메일 받을 사용자
        message.setSubject("회원가입을 위한 이메일 인증코드 입니다"); // 이메일 제목
        StringBuilder msgg = new StringBuilder();
        msgg.append("<div align='center' style='border:1px solid black'>");
        msgg.append("<h3 style='color:blue'>회원가입 인증코드 입니다</h3>");
        msgg.append("<div style='font-size:130%'>");
        msgg.append("<strong>" + ePw + "</strong></div><br/>") ; // 메일에 인증번호 ePw 넣기
        msgg.append("</div>");
        message.setText(msgg.toString(), "utf-8", "html"); // 메일 내용, charset타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("pajang1514@naver.com", "Admin"));
        return message;
    }
    // 랜덤 인증코드 생성
    public String createKey() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String key = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        //System.out.println("생성된 랜덤 인증코드"+ key);
        return key;
    }
    // 메일 발송
    // sendSimpleMessage 의 매개변수 to는 이메일 주소가 되고,
    // MimeMessage 객체 안에 내가 전송할 메일의 내용을 담는다
    // bean으로 등록해둔 javaMail 객체를 사용하여 이메일을 발송한다
    public String sendSimpleMessage(String to) throws Exception {
        ePw = createKey(); // 랜덤 인증코드 생성
        //System.out.println("********생성된 랜덤 인증코드******** => " + ePw);
        MimeMessage message = null;
        message = creatMessage(to);
        //System.out.println("********생성된 메시지******** => " + message);
        try { // 예외처리
            //System.out.printf(String.valueOf(message));
            emailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException();
        }
        return ePw; // 메일로 사용자에게 보낸 인증코드를 서버로 반환 인증코드 일치여부를 확인하기 위함
    }
}