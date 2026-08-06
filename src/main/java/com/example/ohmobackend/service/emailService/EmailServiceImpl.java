package com.example.ohmobackend.service.emailService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.AuthHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String SENDGRID_ENDPOINT = "https://api.sendgrid.com/v3/mail/send";

    private final RestTemplate sendGridRestTemplate;

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    public EmailServiceImpl(@Qualifier("sendGridRestTemplate") RestTemplate sendGridRestTemplate) {
        this.sendGridRestTemplate = sendGridRestTemplate;
    }

    @Override
    public void sendVerificationCode(String toEmail, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "personalizations", List.of(Map.of("to", List.of(Map.of("email", toEmail)))),
                "from", Map.of("email", fromEmail),
                "subject", "[Oh-Mo] 비밀번호 찾기 인증 코드",
                "content", List.of(Map.of(
                        "type", "text/html",
                        "value",
                        "<p>안녕하세요, Oh-Mo입니다.</p>"
                                + "<p>비밀번호 재설정을 위한 인증 코드입니다.</p>"
                                + "<h2>" + code + "</h2>"
                                + "<p>해당 코드는 <strong>5분간</strong> 유효합니다.</p>"
                ))
        );

        try {
            ResponseEntity<String> response = sendGridRestTemplate.postForEntity(
                    SENDGRID_ENDPOINT, new HttpEntity<>(body, headers), String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new AuthHandler(ErrorStatus.EMAIL_SEND_FAILED);
            }
        } catch (RestClientException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }
}
