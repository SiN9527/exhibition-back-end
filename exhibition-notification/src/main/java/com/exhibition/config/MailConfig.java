package com.exhibition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration

public class MailConfig {


    @Bean  // 定義一個 @Bean 方法，Spring 會自動管理這個 Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();  // 建立 JavaMailSenderImpl 物件


        mailSender.setHost("smtp.gmail.com");  // 設定 SMTP 伺服器主機
        mailSender.setPort(587);  // 設定 SMTP 伺服器連接埠
        mailSender.setUsername("hititoffworkspace@gmail.com");  // 設定 SMTP 帳戶
        mailSender.setPassword("gvoz hbuz cdnm gxrm");  // 設定 SMTP 密碼

        Properties props = mailSender.getJavaMailProperties();  // 取得 JavaMailSender 的屬性設定
        props.put("mail.smtp.auth", "true");  // 啟用 SMTP 身份驗證
        props.put("mail.smtp.starttls.enable", "true");  // 啟用 STARTTLS 加密
        props.put("mail.debug", "true");  // 啟用除錯模式，會在控制台輸出詳細的郵件發送資訊

        return mailSender;  // 回傳 JavaMailSender 物件
    }
}

