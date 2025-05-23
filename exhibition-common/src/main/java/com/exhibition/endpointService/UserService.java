package com.exhibition.endpointService;

import com.exhibition.dto.UserDto;

public interface UserService {

    UserDto getUser(String userId);

    //註冊會員請求
    RegiResp userRegister(RegiReq regiReq);
}
