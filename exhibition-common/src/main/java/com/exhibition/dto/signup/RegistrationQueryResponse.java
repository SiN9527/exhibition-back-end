package com.exhibition.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class RegistrationQueryResponse implements Serializable {

    @JsonProperty("MemberMainDto")
    private MemberMainDto memberMainDto;


}