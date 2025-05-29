package com.exhibition.dto.signup;

import com.exhibition.dto.DtoAuditable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Value
public class RegistrationFeeConfigDto  extends DtoAuditable implements Serializable {



    @NotNull
    @JsonProperty("EventId")
    @Size(max = 50)
    String eventId;
    @NotNull
    @JsonProperty("Category")
    @Size(max = 100)
    String category;
    @NotNull
    @JsonProperty("Region")
    String region;
    @NotNull
    @JsonProperty("Phase")
    String phase;
    @NotNull
    @JsonProperty("ExchangeRateUsTw")
    BigDecimal exchangeRateUsTw;
    @NotNull
    @JsonProperty("AmountNtd")
    BigDecimal amountNtd;
    @JsonProperty("AmountUsd")
    BigDecimal amountUsd;
    @JsonProperty("IsActive")
    Boolean isActive;
    @JsonProperty("CreateTime")
    Timestamp createTime;
    @JsonProperty("UpdateTime")
    Timestamp updateTime;
}