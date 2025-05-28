package com.exhibition.service.impl;

import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.service.NotificationEndpointService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationEndpointServiceImpl implements NotificationEndpointService {

    private final JavaMailSender mailSender;

    private final String FRONTEND_URL = "http://localhost:5173"; //前端網址記得改

    public NotificationEndpointServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendTempPasswordEmail(MemberMainEntityDto memberMainEntityDto, String tempPassword, String token, String purpose) {
        String subject = "Temporary Password for AASD 2025";
        String content = """
                <p>Dear %s,</p>
                <p>You requested to reset your password. Please use the following temporary password to log in:</p>
                <p><b>%s</b></p>
                <p>⚠️ For security reasons, you must reset your password immediately after logging in.</p>
                """.formatted(memberMainEntityDto.getLastName(), tempPassword);

        sendEmail(memberMainEntityDto.getEmail(), subject, content);
    }

    @Override
    public void sendResetPwdSuccessEmail(MemberMainEntityDto memberMainEntityDto, String token, String purpose) {
        String subject = "Password Changed Notification";
        String content = "<h3>Dear " + memberMainEntityDto.getFirstName() + " " + memberMainEntityDto.getLastName() + ",</h3>"
                + "<p>Your password has been successfully changed.</p>"
                + "<p>If you did not make this change, please contact us immediately.</p>";


        sendEmail(memberMainEntityDto.getEmail(), subject, content);
    }

    @Override
    public void sendMailVerificationEmail(String newEmail, String token, String purpose) {
        //產生變更 Email 的驗證 token

        String verificationLink = FRONTEND_URL + "/verify?token=" + token;

        //  設定信件內容
        String subject = "Member Account Verification";
        String content = "<h3>Dear Member,</h3>"
                + "<p>Please click the link below to verify your account:</p>"
                + "<p><a href='" + verificationLink + "'>Click here to verify your account</a></p>"
                + "<p>This link will expire in 24 hours.</p>";

        sendEmail(newEmail, subject, content);
    }

    @Override
    public void sendRegistrationVerifyMail(MemberMainEntityDto memberMainEntityDto, String token, String purpose) {
        //產生註冊帳號的驗證 token
        String verificationLink = FRONTEND_URL + "/verify?token=" + token;

        String subject = "Member Account Verification";
        String content = "<h3>Dear Member,</h3>"
                + "<p>Please click the link below to verify your account:</p>"
                + "<p><a href='" + verificationLink + "'>Click here to verify your account</a></p>"
                + "<p>This link will expire in 24 hours.</p>";

        sendEmail(memberMainEntityDto.getEmail(), subject, content);
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
