package com.hititoff.serviceAdapter.client;

import com.hititoff.config.Client;
import com.hititoff.dto.UserDto;
import com.hititoff.dto.auth.RegiReq;
import com.hititoff.dto.auth.RegiResp;
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
