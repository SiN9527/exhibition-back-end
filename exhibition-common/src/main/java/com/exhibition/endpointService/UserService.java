package com.exhibition.endpointService;

import com.exhibition.dto.auth.MemberMainEntityDto;

public interface UserService {



     MemberMainEntityDto getMember(String memberId);
}
