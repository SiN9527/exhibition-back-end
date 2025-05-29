package com.exhibition.serviceAdapter.client;


import com.exhibition.config.Client;
import com.exhibition.dto.auth.MemberMainEntityDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MemberClientConfig {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;
    public MemberClientConfig(@Qualifier("userRestTemplate") RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    @Bean
    public Client<String, MemberMainEntityDto> getMemberClient() {
        return new Client<>(restTemplate, MemberMainEntityDto.class, retryTemplate);
    }

    @Bean
    public Client<String, MemberMainEntityDto> postMemberClient() {
        return new Client<>(restTemplate, MemberMainEntityDto.class, retryTemplate);
    }



}
