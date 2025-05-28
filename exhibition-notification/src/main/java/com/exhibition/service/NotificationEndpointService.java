package com.exhibition.service;

import com.exhibition.dto.auth.MemberMainEntityDto;


public interface NotificationEndpointService {

    void sendTempPasswordEmail(MemberMainEntityDto memberMainEntityDto, String tempPassword, String token, String purpose);

    void sendResetPwdSuccessEmail(MemberMainEntityDto memberMainEntityDto, String token, String purpose);

    void sendMailVerificationEmail(String newEmail, String token, String purpose);

    void sendRegistrationVerifyMail(MemberMainEntityDto memberMainEntityDto, String token, String purpose);

}
