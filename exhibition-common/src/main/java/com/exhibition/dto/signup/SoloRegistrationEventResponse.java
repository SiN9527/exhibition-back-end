package com.exhibition.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SoloRegistrationEventResponse implements Serializable {

    @JsonProperty("RegistrationMainDto")
    private RegistrationMainDto registrationMainDto;

    @JsonProperty("RegistrationPaymentInfoDto")
    private RegistrationPaymentInfoDto registrationPaymentInfoDto;


}