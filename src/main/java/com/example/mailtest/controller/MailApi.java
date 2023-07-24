package com.example.mailtest.controller;

import com.example.mailtest.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailApi {
    // 이메일 인증
    private final MailService mailService;
    @PostMapping("{email}")
    @ResponseBody
    public String mailConfirm(@PathVariable String email) throws Exception{
        return mailService.sendSimpleMessage(email);
    }
}