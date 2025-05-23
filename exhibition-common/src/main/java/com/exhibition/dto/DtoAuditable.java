package com.exhibition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;


@Data
public abstract class DtoAuditable {

    @JsonProperty("CreateTime")
    private Timestamp createTime;
    @JsonProperty("UpdateTime")
    private Timestamp updateTime;
    @JsonProperty("CreatedBy")
    private String createdBy;
    @JsonProperty("UpdatedBy")
    private String updatedBy;

}
