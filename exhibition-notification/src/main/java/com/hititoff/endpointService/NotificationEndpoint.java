package com.hititoff.endpointService;


import com.hititoff.dto.auth.SendMailRequest;
import com.hititoff.enums.TokenPurpose;
import com.hititoff.service.NotificationEndpointService;
import com.hititoff.wrapper.MatchNotificationWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
                    notificationEndpointService.sendRegistrationVerifyMail(sendMailRequest.getUserAccountDto(), sendMailRequest.getToken(), purpose);
                    break;
                case EMAIL_VERIFICATION:
                    notificationEndpointService.sendMailVerificationEmail(sendMailRequest.getEmail(), sendMailRequest.getToken(), purpose);
                    break;
                case PASSWORD_RESET:
                    notificationEndpointService.sendTempPasswordEmail(sendMailRequest.getUserAccountDto(), sendMailRequest.getTempPassword(), sendMailRequest.getToken(), purpose);
                    break;
                case  PASSWORD_SUCCESS:
                    notificationEndpointService.sendResetPwdSuccessEmail(sendMailRequest.getUserAccountDto(), sendMailRequest.getToken(), purpose);
                    break;

                default:
                    return ResponseEntity.badRequest().build();
            }
            // 如果發送成功，返回 200 OK
         return ResponseEntity.ok().build();
     }


    /**
     * 球局成立後發送確認通知信
     */
    @PostMapping("/notifyMatchConfirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void notifyMatchConfirm(
            @RequestBody MatchNotificationWrapper wrapper
    ) {
        // 呼叫實作服務發送信件
        notificationEndpointService.sendMatchConfirmedMail(wrapper.getUsersAccounts(), wrapper);
    }

    /**
     * 球局即將開始時發送提醒通知信
     */
    @PostMapping("/notifyGannaStart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void notifyGannaStart(
            @RequestBody MatchNotificationWrapper wrapper
    ) {
        notificationEndpointService.sendMatchGannaStartMail(wrapper.getUsersAccounts(), wrapper);
    }

}
