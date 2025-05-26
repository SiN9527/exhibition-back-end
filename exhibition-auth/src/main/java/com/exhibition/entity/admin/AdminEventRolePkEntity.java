package com.exhibition.entity.admin;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class AdminEventRolePkEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    private long adminId;

    private String eventId;

    private long roleId;


}
