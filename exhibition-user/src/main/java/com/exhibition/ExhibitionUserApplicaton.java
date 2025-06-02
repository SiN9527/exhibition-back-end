package com.exhibition;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ExhibitionUserApplicaton {
    public static void main(String[] args) {
        SpringApplication.run(ExhibitionUserApplicaton.class, args);
    }

    @Bean
    public CommandLineRunner testMapperLoaded(ApplicationContext ctx) {
        return args -> {
            System.out.println("==== 檢查 AdminMainMapper Bean ====");
            String[] names = ctx.getBeanNamesForType(com.exhibition.mapper.AdminMainMapper.class);
            for (String name : names) {
                System.out.println("✅ 找到 Bean: " + name);
            }
            if (names.length == 0) {
                System.out.println("❌ 沒有找到 AdminMainMapper Bean！");
            }
        };
    }
}