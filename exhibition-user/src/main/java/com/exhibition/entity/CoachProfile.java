package com.exhibition.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coach_profile", schema = "hititoff_dev")
public class CoachProfile  {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "license")
    private String license;

    @Lob
    @Column(name = "experience")
    private String experience;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "status", length = 20)
    private String status;

    @Lob
    @Column(name = "intro")
    private String intro;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

}