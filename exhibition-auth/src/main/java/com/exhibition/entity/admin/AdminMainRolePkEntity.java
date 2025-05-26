package com.exhibition.entity.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class AdminMainRolePkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @NotNull
    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private long roleId;

    public AdminMainRolePkEntity() {}

    public AdminMainRolePkEntity(long adminId, Long roleId) {
        this.adminId = adminId;
        this.roleId = roleId;
    }



}
