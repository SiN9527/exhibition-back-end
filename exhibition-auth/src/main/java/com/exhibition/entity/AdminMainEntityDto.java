package com.exhibition.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link AdminMainEntity}
 */
@Value
public class AdminMainEntityDto implements Serializable {
    Long adminId;
    @NotNull
    @Size(max = 50)
    String account;
    @NotNull
    @Size(max = 100)
    String email;
    @NotNull
    @Size(max = 255)
    String password;
    String eventId;
    @NotNull
    @Size(max = 50)
    String userName;
    @Size(max = 50)
    String userDept;
    @NotNull
    Boolean enabled;
    @Size(max = 50)
    String createdBy;
    Timestamp createdAt;
    @Size(max = 50)
    String updatedBy;
    Timestamp updatedAt;
}