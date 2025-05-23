package com.exhibition.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class MemberEventPkEntity implements java.io.Serializable {
    private static final long serialVersionUID = -7664510360659768710L;

    private String memberId;


    private String eventId;



}