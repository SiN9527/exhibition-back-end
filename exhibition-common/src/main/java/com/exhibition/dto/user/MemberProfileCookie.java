package com.exhibition.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MemberProfileCookie {

    @JsonProperty("Email")
    private String email;

    @JsonProperty("MemberType")
    private String memberType;

    @JsonProperty("MemberId")
    private String memberId;
}
