package com.exhibition.serviceAdapter.client;

import com.exhibition.config.Client;
import com.exhibition.dto.UserDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UserClientConfig {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    public UserClientConfig(@Qualifier("userRestTemplate") RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    @Bean
    public Client<String, UserDto> getUserClient() {
        return new Client<>(restTemplate, UserDto.class, retryTemplate);
    }

    @Bean
    public Client<String, UserDto> postUserClient() {
        return new Client<>(restTemplate, UserDto.class, retryTemplate);
    }


    @Bean
    public Client<RegiReq, RegiResp> postUserRegiClient() {
        return new Client<>(restTemplate, RegiResp.class, retryTemplate); // input: RegiReq, output: RegiResp
    }
}
