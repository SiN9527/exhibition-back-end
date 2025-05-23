package com.exhibition.service;

import com.hititoff.dto.UserDto;
import com.hititoff.dto.auth.RegiReq;
import com.hititoff.dto.auth.RegiResp;
import com.hititoff.dto.auth.UserAccountDto;

public interface UserEndpointService {

    RegiResp registerUser(RegiReq regiReq);

    UserDto getUser(String userId);
}
