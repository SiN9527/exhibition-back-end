package com.exhibition.entity.admin;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class AdminEventPkEntity implements java.io.Serializable {
    private static final long serialVersionUID = -7664510360659768710L;

    private String account;


    private String eventId;



}