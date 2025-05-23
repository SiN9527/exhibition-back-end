package com.exhibition.service;

import com.hititoff.dto.auth.CustomUserDetail;
import com.hititoff.dto.user.UserDataQueryDto;
import com.hititoff.dto.user.UserProfileDto;
import com.hititoff.dto.user.UserProfileUpdateReq;
import com.hititoff.dto.user.UserSportProfileUpdateReq;
import com.hititoff.exception.payload.ProcessPayload;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    ProcessPayload<UserDataQueryDto> userDataQuery(CustomUserDetail userDetails);


    ProcessPayload<String> updateUserProfile(CustomUserDetail userDetails, UserProfileUpdateReq userProfileUpdateReq);

    ProcessPayload<String> updateUserSportProfile(CustomUserDetail userDetails, UserSportProfileUpdateReq userSportProfileUpdateReq);
}
