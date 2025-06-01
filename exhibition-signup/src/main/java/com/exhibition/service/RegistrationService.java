package com.exhibition.service;


import com.exhibition.dto.signup.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface RegistrationService {


    RegistrationQueryResponse registerStep1Query(UserDetails userDetails, HttpServletResponse response);



    SoloRegistrationEventResponse registerStep1(SoloRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response);


    GroupRegistrationEventResponse registerGroupStep1(GroupRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response);
}
