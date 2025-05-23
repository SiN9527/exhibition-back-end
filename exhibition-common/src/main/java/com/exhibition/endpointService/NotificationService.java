package com.exhibition.endpointService;

import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.wrapper.MatchNotificationWrapper;

public interface NotificationService {



    void sendTempPasswordEmail(MemberMainEntityDto dto, String tempPassword, String token, String purpose);

    void sendResetPwdSuccessEmail(MemberMainEntityDto dto, String token, String purpose);

    void sendMailVerificationEmail(String newEmail, String token, String purpose);


    void sendMatchConfirmedMail(MatchNotificationWrapper wrapper);

    void sendMatchGannaStartMail(MatchNotificationWrapper wrapper);

    void sendRegistrationVerifyMail(MemberMainEntityDto dto, String token, String purpose);
}
