package com.exhibition.dto.signup;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegistrationPromoDto implements Serializable {


    @NotNull
    @JsonProperty("RegistrationMainDto")
    RegistrationMainDto registrationMainEntityDto;
    @NotNull
    @JsonProperty("PromotionCodeDto")
    PromotionCodeDto promotionCodeDto;

    @JsonProperty("AppliedDiscount")
    Integer appliedDiscount;
}