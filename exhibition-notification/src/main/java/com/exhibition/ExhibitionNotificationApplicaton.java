package com.exhibition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.exhibition"})
public class ExhibitionNotificationApplicaton{
    public static void main(String[] args) {
        SpringApplication.run(ExhibitionNotificationApplicaton.class, args);
    }
}