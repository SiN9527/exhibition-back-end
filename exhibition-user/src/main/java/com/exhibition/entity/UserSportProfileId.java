package com.exhibition.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class UserSportProfileId implements Serializable {
    private static final long serialVersionUID = -5995169402488667987L;

    private String userId;


    private String sportType;



}