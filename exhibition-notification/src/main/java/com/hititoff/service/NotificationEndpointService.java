package com.hititoff.service;

import com.hititoff.dto.auth.UserAccountDto;
import com.hititoff.wrapper.MatchNotificationWrapper;

import java.util.List;

public interface NotificationEndpointService {

    void sendTempPasswordEmail(UserAccountDto userAccountDto, String tempPassword, String token, String purpose);

    void sendResetPwdSuccessEmail(UserAccountDto userAccountDto, String token, String purpose);

    void sendMailVerificationEmail(String newEmail, String token, String purpose);

    void sendRegistrationVerifyMail(UserAccountDto userAccountDto, String token, String purpose);

    void sendMatchConfirmedMail(List<UserAccountDto> users, MatchNotificationWrapper wrapper);

    void sendMatchGannaStartMail(List<UserAccountDto> users, MatchNotificationWrapper wrapper);

}
