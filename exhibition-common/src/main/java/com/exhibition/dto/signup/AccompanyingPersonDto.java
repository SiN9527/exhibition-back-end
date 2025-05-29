package com.exhibition.dto.signup;

import com.exhibition.dto.DtoAuditable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;


@Data
public class AccompanyingPersonDto  extends DtoAuditable implements Serializable {

    @JsonProperty("Seq")
    Integer seq;

    @JsonProperty("MemberFollowedId")
    String memberFollowedId;
    @Size(max = 50)
    @JsonProperty("FullName")
    String fullName;
    @Size(max = 50)
    @JsonProperty("Nationality")
    String nationality;
    @JsonProperty("DateOfBirth")
    Instant dateOfBirth;
    @JsonProperty("IsVegetarian")
    Boolean isVegetarian;
    @Size(max = 50)
    @JsonProperty("PassportOrId")
    String passportOrId;
    @Size(max = 50)
    @JsonProperty("UploadNo")
    String uploadNo;

    @JsonProperty("CreateTime")
    Timestamp createTime;
}