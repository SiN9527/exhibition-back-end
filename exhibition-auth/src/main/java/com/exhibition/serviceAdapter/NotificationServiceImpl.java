package com.exhibition.serviceAdapter;


import com.exhibition.config.Client;
import com.exhibition.endpointService.NotificationService;
import com.exhibition.wrapper.MatchNotificationWrapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {


    private final Client<SendMailRequest, Void> postSendMailClient; // 修正：加入這個依賴

    public NotificationServiceImpl( @Qualifier("postSendMailClient") Client<SendMailRequest, Void> postSendMailClient) {

        this.postSendMailClient = postSendMailClient;
    }


    @Override
    public void sendTempPasswordEmail(UserAccountDto userAccountDto, String tempPassword,String token,String purpose) {
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setUserAccountDto(userAccountDto);
        sendMailRequest.setTempPassword(tempPassword);
        sendMailRequest.setToken(token);
        sendMailRequest.setPurpose(purpose);



        postSendMailClient.post(sendMailRequest,"/endpoint/notification/sendMail");
    }

    @Override
    public void sendResetPwdSuccessEmail(UserAccountDto userAccountDto,String token,String purpose) {
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setUserAccountDto(userAccountDto);
        sendMailRequest.setToken(token);
        sendMailRequest.setPurpose(purpose);

        postSendMailClient.post(sendMailRequest,"/endpoint/notification/sendMail");
    }

    @Override
    public void sendMailVerificationEmail(String newEmail,String token,String purpose) {
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setEmail(newEmail);
        sendMailRequest.setToken(token);
        sendMailRequest.setPurpose(purpose);
        postSendMailClient.post(sendMailRequest,"/endpoint/notification/sendMail");
    }

    @Override
    public void sendRegistrationVerifyMail(UserAccountDto userAccountDto,String token,String purpose) {
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setUserAccountDto(userAccountDto);
        sendMailRequest.setToken(token);
        sendMailRequest.setPurpose(purpose);

        postSendMailClient.post(sendMailRequest, "/endpoint/notification/sendMail");
    }
    @Override
    public void sendMatchConfirmedMail(MatchNotificationWrapper wrapper) {
        throw new NotSupportedServiceException("auth service 不支援 send match confirm mail");
    }

    @Override
    public void sendMatchGannaStartMail(MatchNotificationWrapper wrapper) {
        throw new NotSupportedServiceException("auth service 不支援 send match match ganna start mail");
    }
}
