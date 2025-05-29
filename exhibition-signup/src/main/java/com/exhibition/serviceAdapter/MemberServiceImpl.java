package com.exhibition.serviceAdapter;


import com.exhibition.config.Client;
import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.endpointService.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements UserService {

    private final Client<String, MemberMainEntityDto> getMemberClient;
    private final Client<String, MemberMainEntityDto> postMemberClient;


    public MemberServiceImpl(
            @Qualifier("getMemberClient") Client<String, MemberMainEntityDto> getMemberClient,
            @Qualifier("postMemberClient") Client<String, MemberMainEntityDto> postMemberClient

    ) {
        this.getMemberClient = getMemberClient;
        this.postMemberClient = postMemberClient;

    }

    @Override
    public MemberMainEntityDto getMember(String userId) {
        return getMemberClient.post(userId, "/endpoint/user/getMember").getBody();
    }


}
