package com.exhibition.enums;


import lombok.Getter;

import java.util.Objects;

@Getter
public enum TokenPurpose {

    ACCOUNT_REGISTRATION("accountRegistration"),
    EMAIL_VERIFICATION("emailVerification"),
    PASSWORD_RESET("passwordReset"),
    PASSWORD_SUCCESS("passwordSuccess"),
    ;

    private final String value;

    TokenPurpose(String value) {
        this.value = value;
    }

    // 提供一個靜態方法，從 purpose 找到對應的 enum
    public static TokenPurpose fromPurpose(String purpose) {
        for (TokenPurpose tokenPurpose : TokenPurpose.values()) {  // 遍歷所有 成員
            if (Objects.equals(tokenPurpose.getValue(), purpose)) {
                return tokenPurpose; // 找到符合的，回傳
            }
        }
        // 如果沒有找到，可以選擇丟出例外
        throw new IllegalArgumentException("Invalid purpose: " + purpose);
    }

}