package com.exhibition.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class GroupRegistrationEventRequest implements Serializable {

    @JsonProperty("GroupRegistrationMainDto")
    private List<GroupRegistrationMainDto> groupRegistrationMainDto;

    @JsonProperty("RegistrationPaymentInfoDto")
    private RegistrationPaymentInfoDto registrationPaymentInfoDto;








}