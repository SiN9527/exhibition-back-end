package com.exhibition.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminMemberDeleteRequest {

    @JsonProperty("EventId")
    private String eventId;

    @JsonProperty("MemberId")
    private String memberId;

    /**
     * 信箱 (必填)
     * - 必須為有效的 Email 格式
     */
    @JsonProperty("Email")
    private String email;



}
