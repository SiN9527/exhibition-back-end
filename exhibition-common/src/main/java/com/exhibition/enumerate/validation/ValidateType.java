package com.exhibition.enumerate.validation;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ValidateType {
    FIELD_REQUIRED,
    TIME_OUT_OF_RANGE,
    NOT_VALID_NUMBER,
    ;

    private String value;

    public static ValidateType of(String value) {
        return Arrays.stream(ValidateType.values())
                .filter(vt -> vt.getValue().equals(value))
                .findAny().orElseThrow(() ->
                        new EnumMappingException("illegal argument :" + value + " of ValidateType", ValidateType.class));

    }
}
