package com.exhibition.enumerate.match;

import java.util.Arrays;

public enum MatchJoinStatus {

    JOINED,
    CANCELLED;

    private String value;

    public static MatchJoinStatus of(String value) {
        return Arrays.stream(MatchJoinStatus.values()).filter(x -> x.value.equals(value)).findAny()
                .orElseThrow(() -> new EnumMappingException("illegal argument :" + value + " of MatchJoinStatus", MatchJoinStatus.class));
    }
}
