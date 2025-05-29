package com.exhibition.dto.signup;


import com.exhibition.dto.DtoAuditable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegistrationPromoDto  extends DtoAuditable implements Serializable {


    @NotNull
    @JsonProperty("RegistrationMainDto")
    RegistrationMainDto registrationMainEntityDto;
    @NotNull
    @JsonProperty("PromotionCodeDto")
    PromotionCodeDto promotionCodeDto;

    @JsonProperty("AppliedDiscount")
    Integer appliedDiscount;
}