package com.exhibition.endpointController;


import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.endpointService.EndpointService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/endpoint/user")
public class UserEndpointController {

    private final EndpointService endpointService;

    public UserEndpointController(EndpointService endpointService) {
        this.endpointService = endpointService;
    }

    @PostMapping("/getMember")
    public MemberMainEntityDto getMember(String userId) {
        return endpointService.memberGetProfile(userId);
    }

}
