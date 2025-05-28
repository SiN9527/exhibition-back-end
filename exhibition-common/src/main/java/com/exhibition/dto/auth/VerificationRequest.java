package com.exhibition.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerificationRequest {

    @JsonProperty("Token")
    private String token;
}
