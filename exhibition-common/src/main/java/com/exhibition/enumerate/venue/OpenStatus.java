package com.exhibition.enumerate.venue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum OpenStatus {
    OPEN_FREE("OPEN_FREE", "免費對外開放使用"),
    OPEN("OPEN", "對外開放使用"),
    NOT_OPEN("NOT_OPEN", "不對外開放使用");

    private final String value;
    private final String srcCaption;

    public static OpenStatus ofValue(String value) {
        for (OpenStatus status : OpenStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new EnumMappingException("Openstatus can't map with value " + value, OpenStatus.class);
    }

    public static OpenStatus ofSrcCaption(String srcCaption) {
        for (OpenStatus status : OpenStatus.values()) {
            if (status.srcCaption.equals(srcCaption)) {
                log.info("srcCaption:{}, status", srcCaption, status);
                return status;
            }
        }
        log.info("srcCaption:{}", srcCaption);
        throw new EnumMappingException("Openstatus can't map with srcCaption " + srcCaption, OpenStatus.class);
    }
}
