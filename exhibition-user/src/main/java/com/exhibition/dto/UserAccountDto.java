package com.exhibition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.hititoff.entity.UserAccount}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto extends DtoAuditable implements Serializable {

    private Long userId;

    private String username;

    private String email;

    private String phone;

    private String password;

    private String oauthType;

    private String oauthId;

    private String status;

    private Timestamp lastLoginTime;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;
}