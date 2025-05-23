package com.exhibition.serviceAdapter;


import com.exhibition.config.Client;
import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.dto.mail.SendMailRequest;
import com.exhibition.endpointService.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {


    private final Client<SendMailRequest, Void> postSendMailClient; // 修正：加入這個依賴

    public NotificationServiceImpl( @Qualifier("postSendMailClient") Client<SendMailRequest, Void> postSendMailClient) {

        this.postSendMailClient = postSendMailClient;
    }


    @Override
    public void sendTempPasswordEmail(MemberMainEntityDto dto, String tempPassword, String token, String purpose) {
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setMemberMainEntityDto(dto);
        sendMailRequest.setTempPassword(tempPassword);
        sendMailRequest.setToken(token);
        sendMailRequest.setPurpose(purpose);



        postSendMailClient.post(sendMailRequest,"/endpoint/notification/sendMail");
    }

    @Override
    public void sendResetPwdSuccessEmail(MemberMainEntityDto dto,String token,String purpose) {
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setMemberMainEntityDto(dto);
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
    public void sendRegistrationVerifyMail(MemberMainEntityDto dto, String token, String purpose) {
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setMemberMainEntityDto(dto);
        sendMailRequest.setToken(token);
        sendMailRequest.setPurpose(purpose);

        postSendMailClient.post(sendMailRequest, "/endpoint/notification/sendMail");
    }

}
