package com.exhibition.entity.member;

import com.exhibition.entity.base.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Entity
@Table(name = "MEMBER_MAIN_ROLE", schema = "ems_001")
public class MemberMainRoleEntity extends Auditable {

    @EmbeddedId
    private MemberMainRolePkEntity pk; // 複合主鍵

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @Size(max = 50)
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "update_time")
    private Timestamp updateTime;

    /**
     * 設定 `create_time` 和 `update_time` 預設值
     */
    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createTime = now;

    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = new Timestamp(System.currentTimeMillis());
    }



}