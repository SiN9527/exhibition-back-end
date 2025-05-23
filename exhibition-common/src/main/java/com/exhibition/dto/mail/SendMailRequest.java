package com.exhibition.dto.mail;

import com.exhibition.dto.auth.AdminMainEntityDto;
import com.exhibition.dto.auth.MemberMainEntityDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SendMailRequest {

    @JsonProperty("Email")
    private String email;

    @JsonProperty("AdminMainEntityDto")
    private AdminMainEntityDto adminMainEntityDto;

    @JsonProperty("MemberMainEntityDto")
    private MemberMainEntityDto memberMainEntityDto;

    @JsonProperty("TempPassword")
    private String tempPassword;

    @JsonProperty("Token")
    private String token;

    @JsonProperty("Purpose")
    private String purpose;
}
