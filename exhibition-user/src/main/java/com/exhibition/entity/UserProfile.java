package com.exhibition.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_profile", schema = "hititoff_dev")
public class UserProfile extends Auditable{
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "avatar")
    private String avatar;

    @Lob
    @Column(name = "intro")
    private String intro;

    @Column(name = "level", length = 20)
    private String level;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}