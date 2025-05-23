package com.exhibition.endpointService;

import com.hititoff.dto.auth.RegiReq;
import com.hititoff.dto.auth.RegiResp;
import com.hititoff.dto.auth.UserAccountDto;
import com.hititoff.dto.UserDto;
import com.hititoff.service.UserEndpointService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/endpoint/user")
public class UserEndpoint {


    private final UserEndpointService userEndpointService;

    public UserEndpoint(UserEndpointService userEndpointService) {

        this.userEndpointService = userEndpointService;
    }


    @PostMapping("/register")
    public RegiResp userRegister( @RequestBody RegiReq regiReq) {
        return userEndpointService.registerUser(regiReq);
    }

    @PostMapping("/getUser")
    public UserDto getUser( @RequestBody String userId) {
        return userEndpointService.getUser(userId);
    }
}
