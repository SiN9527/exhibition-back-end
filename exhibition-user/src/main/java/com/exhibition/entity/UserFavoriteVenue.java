package com.exhibition.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_favorite_venue", schema = "hititoff_dev")
public class UserFavoriteVenue  {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "venue_id", nullable = false)
    private Long venueId;

    @Column(name = "create_time")
    private Timestamp createTime;

}