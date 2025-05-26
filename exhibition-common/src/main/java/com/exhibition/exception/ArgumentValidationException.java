package com.exhibition.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ArgumentValidationException extends RuntimeException {

    // 只適用於驗證由前端發送的資料驗證
    private final List<String> invalidFields;
    private final List<String> invalidReasons;

    public ArgumentValidationException(final List<String> invalidFields, final List<String> invalidReasons) {
        this.invalidFields = invalidFields;
        this.invalidReasons = invalidReasons;
    }

    public ArgumentValidationException(final List<String> invalidFields, final List<String> invalidReasons, Throwable throwable) {
        super(throwable);
        this.invalidFields = invalidFields;
        this.invalidReasons = invalidReasons;
    }

    public ArgumentValidationException(final String invalidField, final String invalidReason) {
        this.invalidFields = List.of(invalidField);
        this.invalidReasons = List.of(invalidReason);
    }

    public ArgumentValidationException(final String invalidField, final String invalidReason, final Throwable throwable) {
        super(throwable);
        this.invalidFields = List.of(invalidField);
        this.invalidReasons = List.of(invalidReason);
    }
}
