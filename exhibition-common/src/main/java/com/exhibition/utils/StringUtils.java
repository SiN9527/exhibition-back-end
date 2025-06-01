package com.exhibition.utils;

public class StringUtils {
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static String nullElse(String str, String orElse) {
        return str == null ? orElse : str;
    }

    public static String shrink(String s, int i) {
        if (s == null || s.length() <= i) {
            return s;
        }
        return s.substring(0, i);
    }
}
