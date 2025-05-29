package com.exhibition.service;


import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.signup.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {


    RegistrationQueryResponse registerStep1Query(UserDetails userDetails, HttpServletResponse response);


    @Transactional
    SoloRegistrationEventResponse registerStep1(SoloRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response);

    @Transactional
    GroupRegistrationEventResponse registerGroupStep1(GroupRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response);
}
