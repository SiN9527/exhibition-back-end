package com.exhibition.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.svc.ems.entity.MemberRoleEventEntity}
 */
@Data
public class SoloRegistrationEventRequest implements Serializable {

    @JsonProperty("RegistrationMainDto")
    private RegistrationMainDto registrationMainDto;

    @JsonProperty("RegistrationDetailDto")
    private RegistrationDetailDto registrationDetailDto;

    @JsonProperty("RegistrationExtraDto")
    private RegistrationExtraDto registrationExtraDto;

    @JsonProperty("RegistrationPaymentInfoDto")
    private RegistrationPaymentInfoDto registrationPaymentInfoDto;

    @JsonProperty("AccompanyingPersonDtoList")
    private List<AccompanyingPersonDto> accompanyingPersonDtoList;


}