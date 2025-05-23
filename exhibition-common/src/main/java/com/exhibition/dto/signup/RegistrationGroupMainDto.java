package com.exhibition.dto.signup;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svc.ems.entity.RegistrationGroupMainEntity;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link RegistrationGroupMainEntity}
 */
@Value
public class RegistrationGroupMainDto implements Serializable {
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
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
    @JsonProperty("    Timestamp UpdatedAt;\n")
    Timestamp updatedAt;
}