package com.hititoff.service.impl;

import com.hititoff.dto.auth.UserAccountDto;
import com.hititoff.dto.match.MatchDto;
import com.hititoff.dto.venue.VenueDto;
import com.hititoff.service.NotificationEndpointService;
import com.hititoff.util.DateTimeUtils;
import com.hititoff.wrapper.MatchNotificationWrapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationEndpointServiceImpl implements NotificationEndpointService {

    private final JavaMailSender mailSender;

    private final String FRONTEND_URL = "http://localhost:3000"; //前端網址記得改

    public NotificationEndpointServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendTempPasswordEmail(UserAccountDto userAccountDto, String tempPassword, String token, String purpose) {
        String subject = "【HIT IT OFF】臨時密碼通知";
        String body = "您好，" + userAccountDto.getUsername() + "，\n\n" +
                "以下是您的臨時密碼，請儘快登入後修改密碼：\n\n" +
                tempPassword + "\n\n" +
                "為了您的帳號安全，請於登入後立即變更密碼。";

        sendEmail(userAccountDto.getEmail(), subject, body);
    }

    @Override
    public void sendResetPwdSuccessEmail(UserAccountDto userAccountDto, String token, String purpose) {
        String subject = "【HIT IT OFF】密碼重設成功通知";
        String body = "您好，" + userAccountDto.getUsername() + "，\n\n" +
                "您的密碼已成功重設。\n\n" +
                "若非您本人操作，請盡快聯繫客服！";

        sendEmail(userAccountDto.getEmail(), subject, body);
    }

    @Override
    public void sendMailVerificationEmail(String newEmail, String token, String purpose) {
        //產生變更 Email 的驗證 token

        String verificationLink = FRONTEND_URL + "/verify?token=" + token;

        String subject = "【HIT IT OFF】新郵箱驗證信";
        String body = "您好，\n\n" +
                "請點擊下方連結以完成新的電子郵件驗證：\n\n" +
                verificationLink + "\n\n" +
                "此連結 24 小時內有效，若超過請重新申請。";

        sendEmail(newEmail, subject, body);
    }

    @Override
    public void sendRegistrationVerifyMail(UserAccountDto userAccountDto, String token, String purpose) {
        //產生註冊帳號的驗證 token
        String verificationLink = FRONTEND_URL + "/verify?token=" + token;

        String subject = "【HIT IT OFF】帳號註冊驗證信";
        String body = "您好，" + userAccountDto.getUsername() + "，\n\n" +
                "感謝您註冊 HIT IT OFF 運動交友平台！\n\n" +
                "請點擊下方連結以完成帳號啟用：\n\n" +
                verificationLink + "\n\n" +
                "此連結將在 24 小時後失效，請儘快完成驗證。";

        sendEmail(userAccountDto.getEmail(), subject, body);
    }

    @Override
    public void sendMatchConfirmedMail(List<UserAccountDto> users, MatchNotificationWrapper wrapper) {
        String startTime = DateTimeUtils.formatOf(wrapper.getStartTime(), DateTimeUtils.PATTERN_YYYY_MM_dd_HH_mm);
        String endTime = DateTimeUtils.formatOf(wrapper.getEndTime(), DateTimeUtils.PATTERN_YYYY_MM_dd_HH_mm);

        VenueDto venue = wrapper.getVenueDto();
        MatchDto match = wrapper.getMatchDto();
        String venueName    = venue.getName();
        String venueAddress = venue.getAddress();
        int    currentPlayers  = match.getCurrentPlayers();
        String organizerName = wrapper.getOrganizerName();
        String organizerLine = wrapper.getOrganizerLine();
        String note         = wrapper.getNote();
        String fee         = wrapper.getFee().toString();

        for (var userAccount : users) {
            String body = "您好，" + userAccount.getUsername() + ",\n\n"
                    + "親愛的會員您好：\n" +
                    "感謝您報名參與球聚交流！\n" +
                    "\n" +
                    "我們很高興地通知您，您所參與的球團行程 已正式確認開始，相關出發資訊如下："
                    + "主持人姓名：" + organizerName + "\n"
                    + "主持人聯絡方式：" + organizerLine + "\n"
                    + "球敘時間：" + startTime + " - " + endTime+"\n"
                    + "場地位置：" + venueName + "\n"
                    + "球場地址：" + venueAddress + "\n"
                    + "球場地址：" + venueAddress + "\n"
                    + "報名人數：" + currentPlayers + "\n"
                    + "報名費用：" + fee + "\n"
                    + "場地描述：" + note + "\n"
                    + "請您務必準時報到，並留意後續將寄送的詳細行程通知與注意事項。如有任何問題，歡迎與我們聯繫。";

            sendEmail(userAccount.getEmail(), "【HIT IT OFF】開團確認通知信", body);
        }
    }

    @Override
    public void sendMatchGannaStartMail(List<UserAccountDto> users, MatchNotificationWrapper wrapper) {

        String startTime = DateTimeUtils.formatOf(wrapper.getStartTime(), DateTimeUtils.PATTERN_YYYY_MM_dd_HH_mm);
        String endTime = DateTimeUtils.formatOf(wrapper.getEndTime(), DateTimeUtils.PATTERN_YYYY_MM_dd_HH_mm);
        VenueDto venue = wrapper.getVenueDto();
        MatchDto match = wrapper.getMatchDto();
        String venueName    = venue.getName();
        String venueAddress = venue.getAddress();
        int    currentPlayers  = match.getCurrentPlayers();
        String organizerName = wrapper.getOrganizerName();
        String organizerLine = wrapper.getOrganizerLine();
        String note         = wrapper.getNote();
        String fee         = wrapper.getFee().toString();

        for (var userAccount : users) {
            String body = "您好，" + userAccount.getUsername() + ",\n\n"
                    + "親愛的會員您好：\n" +
                    "感謝您報名參與球聚交流！\n" +
                    "\n" +
                    "我們很高興地通知您，您所參與的球團行程 已正式確認開始，相關出發資訊如下："
                    + "主持人姓名：" + organizerName + "\n"
                    + "主持人聯絡方式：" + organizerLine + "\n"
                    + "球敘時間：" + startTime + " - " + endTime+"\n"
                    + "場地位置：" + venueName + "\n"
                    + "球場地址：" + venueAddress + "\n"
                    + "球場地址：" + venueAddress + "\n"
                    + "報名人數：" + currentPlayers + "\n"
                    + "報名費用：" + fee + "\n"
                    + "場地描述：" + note + "\n"
                    + "請您務必準時報到，並留意後續將寄送的詳細行程通知與注意事項。如有任何問題，歡迎與我們聯繫。";

            sendEmail(userAccount.getEmail(), "【HIT IT OFF】球團活動開始通知信", body);
        }
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // 使用 HTML 格式
            helper.setFrom("hititoffworkspace@gmail.com");
            mailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }


    private void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);

            log.info("成功寄送郵件至: {}", to);
        } catch (Exception e) {
            log.error("發送郵件失敗: {}", e.getMessage(), e);
        }
    }
}
