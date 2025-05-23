package com.exhibition.endpointService;

import com.exhibition.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endpoint/user")
public class UserEndpoint {

    @PostMapping("/getUser")
    public UserDto getUser(Long userId) {
        return new UserDto(88L, "user from user.userEndpoint");
    }
}
