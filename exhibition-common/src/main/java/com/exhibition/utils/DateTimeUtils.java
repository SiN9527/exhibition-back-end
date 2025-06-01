package com.exhibition.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static final String PATTERN_YYYY_MM_dd_HH_mm = "yyyy/MM/dd HH:mm";

    public static String formatOf(LocalDateTime dateTime, String pattern) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }
}
