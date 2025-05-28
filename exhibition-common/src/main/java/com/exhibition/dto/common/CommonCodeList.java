package com.exhibition.dto.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonCodeList implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("CodeType")
    private String codeType;

    @JsonProperty("CodeOption")
    private String codeOption;

    @JsonProperty("Seq")
    private Integer indCodeseq;

    @JsonProperty("CodeName")
    private String codeName;

    @JsonProperty("CodeTag")
    private String codeTag;

    @JsonProperty("LangKey")
    private String langKey;
}
