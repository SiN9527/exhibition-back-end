package com.exhibition.config;

import com.exhibition.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final RestTemplateBuilder restTemplateBuilder;
    private final RequestUtils requestUtils;

    @Value("${micro.user.url}")
    private String userMicroServiceUrl;


    @Value("${micro.notification.url}")
    private String notificationMicroServiceUrl;


    /**
     * 自訂一個 Interceptor，自動從當前 request 抓 JWT 加進 Authorization Header
     */
    private ClientHttpRequestInterceptor authTokenInterceptor() {
        return (request, body, execution) -> {
            HttpServletRequest currentRequest = requestUtils.getCurrentHttpRequest();
            if (currentRequest != null) {
                String token = currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
                if (token != null && !token.isEmpty()) {
                    request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
                }
            }
            return execution.execute(request, body);
        };
    }

    @Bean(name = "userRestTemplate")
    public RestTemplate userRestTemplate() {
        RestTemplate build = restTemplateBuilder
                .rootUri(userMicroServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(List.of(authTokenInterceptor(), new LoggingClientHttpRequestInterceptor()))
                .build();
    log.debug("[RestTemplate建立完成] userRestTemplate = {}", build);
        log.info("[RestTemplate建立完成] userMicroServiceUrl = {}", userMicroServiceUrl);
        return build;
    }



    @Bean(name = "notificationRestTemplate")
    public RestTemplate notificationRestTemplate() {
        return restTemplateBuilder
                .rootUri(notificationMicroServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(List.of(authTokenInterceptor()))
                .build();
    }

    /**
     * RetryTemplate：最多重試 3 次，每次間隔 1 秒
     */
    @Bean
    public RetryTemplate retryTemplate() {
        var retryTemplate = new RetryTemplate();

        var backoff = new FixedBackOffPolicy();
        backoff.setBackOffPeriod(1000);
        retryTemplate.setBackOffPolicy(backoff);

        var retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }
}
