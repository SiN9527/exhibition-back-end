package com.exhibition.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "user_login_log", schema = "hititoff_dev")
public class UserLoginLog {
    @Id
    @Column(name = "log_id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_ip", length = 50)
    private String loginIp;

    @Column(name = "device", length = 100)
    private String device;

    @Column(name = "login_time")
    private Timestamp loginTime;

    @Column(name = "login_status", length = 20)
    private String loginStatus;

    @Column(name = "fail_reason")
    private String failReason;

}