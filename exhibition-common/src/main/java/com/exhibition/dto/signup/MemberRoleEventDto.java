package com.exhibition.dto.signup;

import com.exhibition.dto.DtoAuditable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;


@Value
public class MemberRoleEventDto  extends DtoAuditable implements Serializable {
    @JsonProperty("RoleCode")
    @Size(max = 10)
    String roleCode;
    @JsonProperty("MemberId")
    @Size(max = 50)
    String memberId;
    @JsonProperty("EventId")
    @Size(max = 50)
    String eventId;
    @JsonProperty("CreateTime")
    Timestamp createTime;
    @Size(max = 50)
    @JsonProperty("CreatedBy")
    String createdBy;
}