package com.exhibition.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class UserFavoriteVenueId implements Serializable {
    private static final long serialVersionUID = -4971218498928615268L;

    private Long userId;


    private Long venueId;



}