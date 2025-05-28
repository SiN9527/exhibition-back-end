package com.exhibition.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MemberProfileResponse {


    @JsonProperty("MemberType")
    private String memberType;

    @JsonProperty("MemberId")
    private String memberId;

    /**
     * 信箱 (必填)
     * - 必須為有效的 Email 格式
     */

    @JsonProperty("Email")
    private String email;


    /**
     * 稱謂 (Dr., Mr., Ms., etc.)
     */

    @JsonProperty("Title")
    private String title;

    /**
     * 名字
     */

    @JsonProperty("FirstName")
    private String firstName;

    /**
     * 姓氏
     */

    @JsonProperty("LastName")
    private String lastName;

    /**
     * 性別 (必須是 Male, Female 或 Other)
     */

    @JsonProperty("Gender")
    private String gender;

    /**
     * 出生日期 (格式: YYYY-MM-DD)
     */

    @JsonProperty("DateOfBirth")
    private String dateOfBirth;

    /**
     * 部門名稱
     */

    @JsonProperty("Department")
    private String department;

    /**
     * 隸屬組織
     */

    @JsonProperty("AffiliationOrganization")
    private String affiliationOrganization;

    /**
     * 所在城市
     */

    @JsonProperty("CityOfAffiliation")
    private String cityOfAffiliation;

    /**
     * 所在國家
     */

    @JsonProperty("CountryOfAffiliation")
    private String countryOfAffiliation;

    /**
     * 電話號碼 (允許空白或 10~15 位數字)
     */


    @JsonProperty("TelNumber")
    private String telNumber;

    /**
     * 手機號碼 (允許空白或 10~15 位數字)
     */

    @JsonProperty("Mobile")
    private String mobile;

    @JsonProperty("EventId")
    private String eventId;
}
