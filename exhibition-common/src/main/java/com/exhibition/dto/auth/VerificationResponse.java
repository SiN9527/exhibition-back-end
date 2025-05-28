package com.exhibition.dto.auth;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationResponse {
    @JsonProperty("Message")
    private String message;
}
