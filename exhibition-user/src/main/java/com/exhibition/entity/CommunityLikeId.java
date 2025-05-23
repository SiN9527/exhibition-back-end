package com.exhibition.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class CommunityLikeId implements Serializable {
    private static final long serialVersionUID = -2812852702182130066L;

    private Long postId;


    private Long userId;



}