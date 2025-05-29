package com.exhibition.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class AccompanyingPersonPkEntity implements Serializable {
    private static final long serialVersionUID = -2099145621875497104L;

    private Integer seq;


    private String memberFollowedId;



}