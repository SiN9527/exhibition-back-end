package com.exhibition.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;


@Value
public class MemberRoleEventDto implements Serializable {
    @JsonProperty("RoleCode")
    @Size(max = 10)
    String roleCode;
    @JsonProperty("MemberId")
    @Size(max = 50)
    String memberId;
    @JsonProperty("EventId")
    @Size(max = 50)
    String eventId;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
    @Size(max = 50)
    @JsonProperty("CreatedBy")
    String createdBy;
}