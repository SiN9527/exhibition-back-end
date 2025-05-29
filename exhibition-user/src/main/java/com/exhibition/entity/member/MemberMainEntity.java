package com.exhibition.entity.member;

import com.exhibition.dto.user.MemberUpdateRequest;
import com.exhibition.entity.base.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "MEMBER_MAIN", schema = "ems_001")
public class MemberMainEntity extends Auditable {

    @Id
    @Size(max = 50)
    @Column(name = "member_id", nullable = false, length = 50)
    private String memberId;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "event_id", length = 50)
    private String eventId; // UUID 格式的活動 ID

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 20)
    @Column(name = "title", length = 20)
    private String title;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Lob
    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 100)
    @Column(name = "department", length = 100)
    private String department;

    @Size(max = 100)
    @Column(name = "affiliation_organization", length = 100)
    private String affiliationOrganization;

    @Size(max = 50)
    @Column(name = "city_of_affiliation", length = 50)
    private String cityOfAffiliation;

    @Size(max = 50)
    @Column(name = "country_of_affiliation", length = 50)
    private String countryOfAffiliation;

    @Size(max = 30)
    @Column(name = "tel_number", length = 30)
    private String telNumber;

    @Size(max = 30)
    @Column(name = "mobile", length = 30)
    private String mobile;

    @Column(name = "registration_date")
    private Timestamp registrationDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "need_reset_pwd")
    private Boolean needResetPwd;

    @Column(name = "email_temp")
    private String emailTemp;

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Size(max = 50)
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "update_time")
    private Timestamp updateTime;



    public void updateProfile(MemberUpdateRequest req) {
        this.eventId = req.getEventId();
        this.title = req.getTitle();
        this.firstName = req.getFirstName();
        this.lastName = req.getLastName();
        this.gender = req.getGender();
        this.dateOfBirth = LocalDate.parse(req.getDateOfBirth()); // 格式已驗證
        this.department = req.getDepartment();
        this.affiliationOrganization = req.getAffiliationOrganization();
        this.cityOfAffiliation = req.getCityOfAffiliation();
        this.countryOfAffiliation = req.getCountryOfAffiliation();
        this.telNumber = req.getTelNumber();
        this.mobile = req.getMobile();
    }

}