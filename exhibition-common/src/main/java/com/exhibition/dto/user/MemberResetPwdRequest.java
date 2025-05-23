package com.exhibition.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberResetPwdRequest {


    /**
     * 密碼 (必填)
     * - 必須至少 8 碼，包含大小寫與數字
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @JsonProperty("Password")
    private String password;

    /**
     * 密碼 (必填)
     * - 必須至少 8 碼，包含大小寫與數字
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @JsonProperty("NewPassword")
    private String newPassword;

}
