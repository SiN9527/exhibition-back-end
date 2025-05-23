package com.exhibition.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SwaggerUserLoginRequest {
    @Schema(description = "使用者 Email", example = "test@gmail.com")
    private String email;

    @Schema(description = "使用者 Account", example = "admin001")
    private String account;

    @Schema(description = "密碼", example = "test123")
    private String password;
}
