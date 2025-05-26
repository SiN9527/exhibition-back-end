package com.exhibition.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

public class Client<T, R> {
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final Class<R> returnClass;
    private final Logger log = LoggerFactory.getLogger(Client.class);
    private final  RetryTemplate retryTemplate;

    public Client(RestTemplate restTemplate, Class<R> clazz, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.returnClass = clazz;
        this.retryTemplate = retryTemplate;
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
        this.headers.setAcceptCharset(List.of(Charset.defaultCharset()));
    }

    public ResponseEntity<R> post(T requestBody, String relativePath) {
        StopWatch watch = new StopWatch();
        watch.start();

        try {
            return retryTemplate.execute(context -> {
                log.info("[Client] POST 呼叫 URL: {}", relativePath);
                HttpEntity<T> entity = new HttpEntity<>(requestBody);
                ResponseEntity<R> response = restTemplate.postForEntity(relativePath, entity, returnClass);
                log.info("[Client] 回傳狀態: {}, 回傳內容: {}", response.getStatusCode(), response.getBody());
                return response;
            });
        } catch (Exception e) {
            log.error("[Client] 呼叫失敗，URL: {}, 錯誤訊息: {}", relativePath, e.getMessage(), e);
            throw new RuntimeException("遠端 API 錯誤: " + e.getMessage(), e);
        } finally {
            watch.stop();
            log.info("[Client] 呼叫耗時: {} ms", watch.getTotalTimeMillis());
        }
    }

//    public ResponseEntity<R> post(T requestBody, String relativePath) {
//        HttpEntity<T> entity = new HttpEntity<T>(requestBody, headers);
//        return restTemplate.postForEntity(relativePath, entity, returnClass);
//    }

    public ResponseEntity<R> get(String relativePath) {
        return restTemplate.getForEntity(relativePath, returnClass);
    }
}
