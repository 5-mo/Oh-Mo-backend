package com.example.ohmobackend.service.emailService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.AuthHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationCode(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("[Oh-Mo] 비밀번호 찾기 인증 코드");
            helper.setText(
                    "<p>안녕하세요, Oh-Mo입니다.</p>" +
                    "<p>비밀번호 재설정을 위한 인증 코드입니다.</p>" +
                    "<h2>" + code + "</h2>" +
                    "<p>해당 코드는 <strong>5분간</strong> 유효합니다.</p>",
                    true
            );
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }
}
