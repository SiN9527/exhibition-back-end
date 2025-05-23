package com.exhibition.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserSportProfileId.class)
@Entity
@Table(name = "user_sport_profile", schema = "hititoff_dev")
public class UserSportProfile extends Auditable{

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Id
    @Column(name = "sport_type", nullable = false, length = 20)
    private String sportType;

    @Column(name = "playing_years")
    private Integer playingYears;

    @Column(name = "self_rating", length = 20)
    private String selfRating;

    @Column(name = "favorite_racket", length = 100)
    private String favoriteRacket;

    @Column(name = "favorite_player", length = 100)
    private String favoritePlayer;

    @Column(name = "preferred_area", length = 100)
    private String preferredArea;

    @Column(name = "play_style", length = 50)
    private String playStyle;

    @Column(name = "game_type", length = 50)
    private String gameType;

    @Lob
    @Column(name = "intro")
    private String intro;


}