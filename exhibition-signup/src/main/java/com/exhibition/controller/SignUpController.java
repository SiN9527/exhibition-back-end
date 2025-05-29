package com.exhibition.controller;

import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.signup.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open/api/user")
public class SignUpController {


    private final RegistrationService registrationService;


    public RegistrationAuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;

    }

    //登入者單人報名
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/soloRegi")
    @Operation(summary = "登入者單人報名")
    public ResponseEntity<ApiResponseTemplate<SoloRegistrationEventResponse>> soloRegistration(
            @RequestBody SoloRegistrationEventRequest req, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        return registrationService.registerStep1(req,userDetails,response);
    }

    //{ withCredentials: true }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/regiInfo")
    @Operation(summary = "登入者取得報名資料")
    public ResponseEntity<ApiResponseTemplate<RegistrationQueryResponse>> memberGetProfile(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        return registrationService.registerStep1Query(userDetails,response);
    }

    //登入者團體報名
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/multiRegi")
    @Operation(summary = "登入者團體報名")
    ResponseEntity<ApiResponseTemplate<GroupRegistrationEventResponse>> registerGroupStep1(@RequestBody GroupRegistrationEventRequest req, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){
        return registrationService.registerGroupStep1(req,userDetails,response);
    };




}
