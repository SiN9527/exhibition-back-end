package com.exhibition.dto.signup;

import com.exhibition.dto.DtoAuditable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
public class GroupRegistrationMainDto  extends DtoAuditable implements Serializable {

    @JsonProperty("RegistrationId")
    String registrationId;

    @JsonProperty("EventId")
    String eventId;

    @JsonProperty("MemberId")
    String memberId;
    @JsonProperty("GroupCode")

    String groupCode;

    @JsonProperty("RegistrationType")

    String registrationType;

    @JsonProperty("AnyAccompanyingPerson")
    Boolean anyAccompanyingPerson;

    @JsonProperty("IsDomestic")
    Boolean isDomestic;

    @JsonProperty("FeeAmount")
    Integer feeAmount;
    @JsonProperty("PaymentStatus")
    String paymentStatus;

    @JsonProperty("RegistrationStatus")
    String registrationStatus;
    @JsonProperty("CreateTime")
    Timestamp createTime;
    @JsonProperty("UpdateTime")
    Timestamp updateTime;

    @JsonProperty("IsGroupMain")
    Boolean isGroupMain;

    @JsonProperty("RegistrationDetailDto")
    private RegistrationDetailDto registrationDetailDto;

    @JsonProperty("RegistrationExtraDto")
    private RegistrationExtraDto registrationExtraDto;


    @JsonProperty("AccompanyingPersonDtoList")
    private List<AccompanyingPersonDto> accompanyingPersonDtoList;

}