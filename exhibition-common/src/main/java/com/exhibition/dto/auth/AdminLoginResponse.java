package com.exhibition.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResponse {



    @JsonProperty("Roles")
    private List<String> roles; // 用戶的角色清單

    @JsonProperty("UserName")
    private String userName; // 用戶名或 email

    @JsonProperty("AdminId")
    private Long adminId; // 用戶 ID (可選)

    @JsonProperty("EventId")
    private String eventId; // 活動 ID (可選)

    @JsonProperty("EventName")
    private String eventName; // 活動名稱 (可選)

    public AdminLoginResponse( List<String> roles) {

        this.roles = roles;
    }
}
