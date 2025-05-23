package com.exhibition.enumerate.match;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MatchStatus {
    OPEN,
    CLOSED,
    FINISHED,
    NOTIFY_CONFIRM,
    NOTIFY_GANNA_START
    ;

    private String value;

    public static MatchStatus of(String value) {
        return Arrays.stream(MatchStatus.values())
                .filter(s -> s.value.equals(value)).findAny()
                .orElseThrow(() -> new EnumMappingException("illegal argument :" + value + " of MatchStatus", MatchStatus.class));
    }
}
