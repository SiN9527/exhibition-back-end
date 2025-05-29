package com.exhibition.entity;

import com.exhibition.entity.base.Auditable;
import com.exhibition.entity.member.MemberMainEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "LOGIN_HISTORY", schema = "ems_001")
public class LoginHistoryEntity extends Auditable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "member_id")
    private Long memberId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "login_time")
    private Timestamp loginTime;

    @Size(max = 45)
    @NotNull
    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @NotNull
    @Lob
    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private MemberMainEntity member;

}