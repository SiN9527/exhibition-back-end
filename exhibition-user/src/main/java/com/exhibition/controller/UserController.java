package com.exhibition.controller;

import com.hititoff.dto.auth.CustomUserDetail;
import com.hititoff.dto.auth.UserAccountDto;
import com.hititoff.dto.user.UserDataQueryDto;
import com.hititoff.dto.user.UserProfileDto;
import com.hititoff.dto.user.UserProfileUpdateReq;
import com.hititoff.dto.user.UserSportProfileUpdateReq;
import com.hititoff.exception.payload.ProcessPayload;
import com.hititoff.service.UserEndpointService;
import com.hititoff.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/open/api/user")
public class UserController {

private final UserService userService;

@PreAuthorize("isAuthenticated()")
@GetMapping("/profile")
public ProcessPayload<UserDataQueryDto> userDataQuery(@AuthenticationPrincipal CustomUserDetail userDetails){

    return userService.userDataQuery(userDetails);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateUserProfile")
    public ProcessPayload<String> updateUserProfile(@AuthenticationPrincipal CustomUserDetail userDetails,@RequestBody UserProfileUpdateReq userProfileUpdateReq){
        return userService.updateUserProfile(userDetails, userProfileUpdateReq);
    };

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateUserSportProfile")
    public ProcessPayload<String> updateUserSportProfile(@AuthenticationPrincipal CustomUserDetail userDetails,@RequestBody UserSportProfileUpdateReq userSportProfileUpdateReq){
        return userService.updateUserSportProfile(userDetails, userSportProfileUpdateReq);
    };


}
