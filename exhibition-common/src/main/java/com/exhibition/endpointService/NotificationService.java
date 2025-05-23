package com.exhibition.endpointService;

import com.exhibition.wrapper.MatchNotificationWrapper;

public interface NotificationService {

    void sendTempPasswordEmail(UserAccountDto userAccountDto, String tempPassword, String token, String purpose);

    void sendResetPwdSuccessEmail(UserAccountDto userAccountDto, String token, String purpose);

    void sendMailVerificationEmail(String newEmail, String token, String purpose);

    void sendRegistrationVerifyMail(UserAccountDto userAccountDto, String token, String purpose);

    void sendMatchConfirmedMail(MatchNotificationWrapper wrapper);

    void sendMatchGannaStartMail(MatchNotificationWrapper wrapper);
}
