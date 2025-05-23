package com.exhibition.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * DTO for {@link MemberMainEntity}
 */
@Value
public class MemberMainEntityDto implements Serializable {
    @Size(max = 50)
    String memberId;
    @NotNull
    @Size(max = 100)
    String email;
    String eventId;
    @NotNull
    @Size(max = 255)
    String password;
    @Size(max = 20)
    String title;
    @Size(max = 50)
    String firstName;
    @Size(max = 50)
    String lastName;
    String gender;
    LocalDate dateOfBirth;
    @Size(max = 100)
    String department;
    @Size(max = 100)
    String affiliationOrganization;
    @Size(max = 50)
    String cityOfAffiliation;
    @Size(max = 50)
    String countryOfAffiliation;
    @Size(max = 30)
    String telNumber;
    @Size(max = 30)
    String mobile;
    Timestamp registrationDate;
    Boolean enabled;
    Boolean needResetPwd;
    @Size(max = 50)
    String createdBy;
    Timestamp createdAt;
    @Size(max = 50)
    String updatedBy;
    Timestamp updatedAt;
}