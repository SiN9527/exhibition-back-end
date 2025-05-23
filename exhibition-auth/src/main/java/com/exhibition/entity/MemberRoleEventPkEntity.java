package com.exhibition.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class MemberRoleEventPkEntity implements Serializable {
    private static final long serialVersionUID = 6760098313863708792L;

    private String roleCode;


    private String memberId;


    private String eventId;



}