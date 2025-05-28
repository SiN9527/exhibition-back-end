package com.exhibition.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class CommonCodeResDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("CommonCodeList")
    private List<CommonCodeList> commonCodeList;
}
