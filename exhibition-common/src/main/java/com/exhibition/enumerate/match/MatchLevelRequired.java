package com.exhibition.enumerate.match;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MatchLevelRequired {
    NO_EXPERIENCE_REQUIRED,
    ENTRY_EXPERIENCE_REQUIRED,
    MEDIUM_EXPERIENCE_REQUIRED,
    ADVANCED_EXPERIENCE_REQUIRED,
    EXPERT_EXPERIENCE_REQUIRED;

    private String value;

    public static MatchLevelRequired of(String value) {
        return Arrays.stream(MatchLevelRequired.values())
                .filter(s -> s.getValue().equals(value))
                .findAny().orElseThrow(() ->
                        new EnumMappingException("illegal argument :" + value + " of MatchLevelRequired", MatchLevelRequired.class));
    }

    public boolean isLessThan(MatchLevelRequired required) {
        return this.ordinal() < required.ordinal();
    }

    public boolean isGreaterThan(MatchLevelRequired required) {
        return this.ordinal() > required.ordinal();
    }
}
