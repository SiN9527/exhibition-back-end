package com.exhibition.entity;

import com.exhibition.entity.base.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "AUTH_TOKEN", schema = "ems_001")
public class AuthTokenEntity extends Auditable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "member_id")
    private Long memberId;

    @Size(max = 1024)
    @NotNull
    @Column(name = "token", nullable = false, length = 1024)
    private String token;

    @NotNull
    @Column(name = "expired_at", nullable = false)
    private Timestamp expiredAt;

    @NotNull
    @Column(name = "create_time")
    private Timestamp createTime;


    /**
     * 設定 `create_time` 和 `update_time` 預設值
     */
    @PrePersist
    protected void onCreate() {
        this.createTime = new Timestamp(System.currentTimeMillis());

    }


}