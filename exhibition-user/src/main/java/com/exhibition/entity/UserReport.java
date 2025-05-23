package com.exhibition.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "user_report", schema = "hititoff_dev")
public class UserReport {
    @Id
    @Column(name = "report_id", nullable = false)
    private Long id;

    @Column(name = "report_user_id")
    private Long reportUserId;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "reason")
    private String reason;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

}