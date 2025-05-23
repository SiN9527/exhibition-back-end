package com.exhibition.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class CodeGeneratorUtils {

    public String generateRegNo(String email) {

        String dateStr = new SimpleDateFormat("yyyyMMddMMssss").format(new Date());
        return dateStr + email;

    }


}