package com.exhibition.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


@Data
public class CommonCodeReqDTO {

    @JsonProperty("CodeType")
    private List<String> codeType;
}
