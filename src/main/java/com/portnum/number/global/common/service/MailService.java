package com.portnum.number.global.common.service;

import com.portnum.number.global.aop.annotation.Retry;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private static final String title = "test";

    private final JavaMailSender mailSender;

    @Retry
    @Async
    public void sendEmail(String toEmail, String text){
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(text, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);

            throw new GlobalException(Code.INTERNAL_ERROR, "Email Send Error");
        }
    }
}
