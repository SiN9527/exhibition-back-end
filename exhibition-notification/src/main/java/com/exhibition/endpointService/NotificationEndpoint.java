package com.exhibition.endpointService;


import com.exhibition.dto.mail.SendMailRequest;
import com.exhibition.enums.TokenPurpose;
import com.exhibition.service.NotificationEndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/endpoint/notification")
public class NotificationEndpoint {


    @Autowired
    private NotificationEndpointService notificationEndpointService;

    @PostMapping("/sendMail")
    public ResponseEntity<Void> sendMail(@RequestBody SendMailRequest sendMailRequest) {

        // 這裡可以根據 sendMailRequest 的內容來決定要發送什麼樣的郵件
        String purpose = sendMailRequest.getPurpose();

        if (purpose == null || purpose.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        switch (TokenPurpose.fromPurpose(purpose)) {
            case ACCOUNT_REGISTRATION:
                notificationEndpointService.sendRegistrationVerifyMail(sendMailRequest.getMemberMainEntityDto(), sendMailRequest.getToken(), purpose);
                break;
            case EMAIL_VERIFICATION:
                notificationEndpointService.sendMailVerificationEmail(sendMailRequest.getEmail(), sendMailRequest.getToken(), purpose);
                break;
            case PASSWORD_RESET:
                notificationEndpointService.sendTempPasswordEmail(sendMailRequest.getMemberMainEntityDto(), sendMailRequest.getTempPassword(), sendMailRequest.getToken(), purpose);
                break;
            case PASSWORD_SUCCESS:
                notificationEndpointService.sendResetPwdSuccessEmail(sendMailRequest.getMemberMainEntityDto(), sendMailRequest.getToken(), purpose);
                break;

            default:
                return ResponseEntity.badRequest().build();
        }
        // 如果發送成功，返回 200 OK
        return ResponseEntity.ok().build();
    }


}
