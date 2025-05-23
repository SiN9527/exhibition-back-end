package com.hititoff.controller;

import com.hititoff.dto.auth.LoginReq;
import com.hititoff.dto.auth.RegiReq;
import com.hititoff.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open/api/notification")
@Tag(name = "Notification", description = "訊息推播相關 API")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 使用者註冊
     */
//    @Operation(summary = "使用者註冊")
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegiReq request,
//                                           HttpServletResponse response) {
//        return authService.userAuthRegi(request, response);
//    }



}

