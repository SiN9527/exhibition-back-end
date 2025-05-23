package com.exhibition.serviceAdapter;


import com.exhibition.config.Client;
import com.exhibition.dto.UserDto;
import com.exhibition.endpointService.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Client<String, UserDto> getUserClient;
    private final Client<String, UserDto> postUserClient;
    private final Client<RegiReq, RegiResp> postUserRegiClient; // 修正：加入這個依賴

    public UserServiceImpl(
            @Qualifier("getUserClient") Client<String, UserDto> getUserClient,
            @Qualifier("postUserClient") Client<String, UserDto> postUserClient,
            @Qualifier("postUserRegiClient") Client<RegiReq, RegiResp> postUserRegiClient
    ) {
        this.getUserClient = getUserClient;
        this.postUserClient = postUserClient;
        this.postUserRegiClient = postUserRegiClient;
    }

    @Override
    public UserDto getUser(String userId) {
        return getUserClient.post(userId, "/endpoint/user/getUser").getBody();
    }

    @Override
    public RegiResp userRegister(RegiReq regiReq) {
        return postUserRegiClient.post(regiReq, "/endpoint/user/register").getBody();
    }
}
