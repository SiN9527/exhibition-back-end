package com.exhibition.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class RegistrationExtraDto implements Serializable {

    @JsonProperty("RegistrationId")
    String registrationId;

    @JsonProperty("AttendGalaDinner")
    boolean attendGalaDinner;


    @JsonProperty("AttendTour")
    boolean attendTour;

    @JsonProperty("AttendWelcomeReception")
    boolean attendWelcomeReception;

    @JsonProperty("IsVegetarian")
    boolean isVegetarian;

    @JsonProperty("Fee")
    Integer fee;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
}