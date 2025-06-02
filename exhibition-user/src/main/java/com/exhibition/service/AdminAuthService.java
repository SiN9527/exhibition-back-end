package com.exhibition.service;


import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.AdminRegisterRequest;
import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.dto.user.AdminMemberDeleteRequest;
import com.exhibition.dto.user.AdminMemberListRequest;
import com.exhibition.dto.user.AdminMemberProfileResponse;
import com.exhibition.dto.user.AdminMemberUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AdminAuthService {


    String adminRegister(@RequestBody AdminRegisterRequest req);

    String adminLogin(AdminRegisterRequest req);

    String adminLogout();

    String adminRefreshToken(String refreshToken);

    String adminUpdateProfile(AdminRegisterRequest req);

    List<AdminMemberProfileResponse> adminGetMemberList(AdminMemberListRequest req);


    MemberMainEntityDto adminGetMemberProfile(AdminRegisterRequest req);

    String adminUpdatePwd(AdminRegisterRequest req);


    String adminUpdateMemberProfile(AdminMemberUpdateRequest req);


    String adminDeleteMemberProfile(AdminMemberDeleteRequest req);
}
