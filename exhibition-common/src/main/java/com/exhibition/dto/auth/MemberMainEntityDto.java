package com.exhibition.dto.auth;

import com.exhibition.dto.DtoAuditable;
import com.exhibition.dto.user.MemberUpdateRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberMainEntityDto  extends DtoAuditable implements Serializable {

    @JsonProperty("MemberId")
    String memberId;
    @JsonProperty("Email")
    String email;
    @JsonProperty("EmailTemp")
    String emailTemp;
    @JsonProperty("EventId")
    String eventId;
    @JsonProperty("Password")
    String password;
    @JsonProperty("Title")
    String title;
    @JsonProperty("FirstName")
    String firstName;
    @JsonProperty("LastName")
    String lastName;
    @JsonProperty("Gender")
    String gender;
    @JsonProperty("DateOfBirth")
    LocalDate dateOfBirth;

    @JsonProperty("Department")
    String department;
    @JsonProperty("AffiliationOrganization")
    String affiliationOrganization;
    @JsonProperty("CityOfAffiliation")
    String cityOfAffiliation;
    @JsonProperty("CountryOfAffiliation")
    String countryOfAffiliation;
    @JsonProperty("TelNumber")
    String telNumber;
    @JsonProperty("Mobile")
    String mobile;
    @JsonProperty("RegistrationDate")
    Timestamp registrationDate;
    @JsonProperty("Enabled")
    Boolean enabled;
    @JsonProperty("NeedResetPwd")
    Boolean needResetPwd;

    String createdBy;
    Timestamp createTime;

    String updatedBy;
    Timestamp updateTime;



}