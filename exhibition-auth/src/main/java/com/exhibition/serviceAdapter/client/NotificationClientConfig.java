package com.exhibition.serviceAdapter.client;

import com.exhibition.config.Client;
import com.exhibition.dto.auth.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NotificationClientConfig {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    public NotificationClientConfig(@Qualifier("notificationRestTemplate") RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }




    @Bean
    public Client<SendMailRequest, Void> postSendMailClient() {
        return new Client<>(restTemplate, Void.class, retryTemplate); // input: RegiReq, output: RegiResp
    }
}
