package com.exhibition.dto.signup;


import com.exhibition.dto.DtoAuditable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class RegistrationGroupMainDto  extends DtoAuditable implements Serializable {
    @Size(max = 20)
    @JsonProperty("GroupId")
    String groupId;

    @JsonProperty("EventId")
    @Size(max = 50)
    String eventId;
    @JsonProperty("GroupName")
    @Size(max = 100)
    String groupName;

    @Size(max = 100)
    @JsonProperty("ContactName")
    String contactName;

    @Size(max = 100)
    @JsonProperty("ContactEmail")
    String contactEmail;
    @Size(max = 50)
    @JsonProperty("ContactPhone")
    String contactPhone;
    @JsonProperty("GroupSize")
    Integer groupSize;
    @JsonProperty("PaymentStatus")
    String paymentStatus;
    @JsonProperty("CreateTime")
    Timestamp createTime;
    @JsonProperty("    Timestamp UpdateTime;\n")
    Timestamp updateTime;
}