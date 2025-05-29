package com.exhibition.dto.common;


import com.exhibition.dto.DtoAuditable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonCodeList  extends DtoAuditable  implements Serializable {
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
