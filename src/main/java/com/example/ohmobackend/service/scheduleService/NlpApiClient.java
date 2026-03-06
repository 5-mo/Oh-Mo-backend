package com.example.ohmobackend.service.scheduleService;

import com.example.ohmobackend.apiPayload.code.status.ErrorStatus;
import com.example.ohmobackend.apiPayload.exception.handler.ScheduleHandler;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class NlpApiClient {

    private final RestTemplate nlpRestTemplate;

    @Value("${nlp.api-url}")
    private String nlpApiUrl;

    public NlpApiClient(@Qualifier("nlpRestTemplate") RestTemplate nlpRestTemplate) {
        this.nlpRestTemplate = nlpRestTemplate;
    }

    @CircuitBreaker(name = "nlpApi", fallbackMethod = "fallback")
    public Map<String, Object> extractSchedule(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(text, headers);

        ResponseEntity<Map> response = nlpRestTemplate.postForEntity(
                nlpApiUrl + "/nlp/extract-text", entity, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ScheduleHandler(ErrorStatus.NLP_PARSE_FAILED);
        }

        return (Map<String, Object>) response.getBody().get("result");
    }

    private Map<String, Object> fallback(String text, Exception e) {
        log.error("NLP API 호출 실패: {}", e.getMessage());
        if (e instanceof ResourceAccessException) {
            throw new ScheduleHandler(ErrorStatus.NLP_SERVER_TIMEOUT);
        }
        throw new ScheduleHandler(ErrorStatus.NLP_SERVER_UNAVAILABLE);
    }
}
