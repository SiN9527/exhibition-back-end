package com.exhibition.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class AdminMainEntityDto implements Serializable {

    @JsonProperty("AdminId")
    Long adminId;
    @JsonProperty("Account")
    String account;
    @JsonProperty("Email")
    String email;
    @JsonProperty("Password")
    String password;
    @JsonProperty("EventId")
    String eventId;
    @JsonProperty("UserName")
    String userName;
    @JsonProperty("UserDept")
    String userDept;
    @JsonProperty("Enabled")
    Boolean enabled;
    @JsonProperty("CreatedBy")
    String createdBy;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
    @JsonProperty("UpdatedBy")
    String updatedBy;
    @JsonProperty("UpdatedAt")
    Timestamp updatedAt;
}