package com.exhibition.enumerate.venue;

import com.exhibition.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Slf4j
public enum SportType {
    BASKETBALL("BASKETBALL", "籃球"),
    TENNIS("TENNIS", "網球"),
    BADMINTON("BADMINTON", "羽球"),
    VOLLEYBALL("VOLLEYBALL", "排球"),
    SOCCER("SOCCER", "足球"),
    BILLIARDS("BILLIARDS", "撞球"),
    TABLE_TENNIS("TABLE_TENNIS", "桌球"),
    SOFTBALL("SOFTBALL", "壘球"),
    BOCCE("BOCCE", "滾球");

    private final String value;
    private final String srcCaption;

    public static SportType ofValue(String value) {
        for (SportType status : SportType.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new EnumMappingException("SportType can't map with value " + value, SportType.class);
    }

    public static SportType ofSrcCaption(String srcCaption) {
        for (SportType status : SportType.values()) {
            if (status.srcCaption.equals(srcCaption)) {
                return status;
            }
        }
        throw new EnumMappingException("SportType can't map with srcCaption " + srcCaption, SportType.class);
    }

    public static SportType ofSrcCaptionLike(String srcCaption) {
        for (SportType status : SportType.values()) {
            if (StringUtils.isNotBlank(srcCaption) && srcCaption.contains(status.getSrcCaption())) {
                return status;
            }
        }
        throw new EnumMappingException("SportType can't map with srcCaption " + srcCaption, SportType.class);
    }
}
