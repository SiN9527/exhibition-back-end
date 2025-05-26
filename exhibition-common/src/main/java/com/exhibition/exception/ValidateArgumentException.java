package com.exhibition.exception;

import lombok.Getter;

// 針對輸入的參數驗證錯誤，適用於格式、範圍錯誤
@Getter
public class ValidateArgumentException extends RuntimeException {

    private String invalidArgumentName;
    // TODO 考慮使用enum統一 validate 錯誤
    private String invalidReason;

    public ValidateArgumentException(String invalidArgumentName, String invalidReason) {
        this.invalidArgumentName = invalidArgumentName;
        this.invalidReason = invalidReason;
    }
}
