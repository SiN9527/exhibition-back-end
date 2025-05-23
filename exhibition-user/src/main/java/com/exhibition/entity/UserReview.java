package com.exhibition.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "user_review", schema = "hititoff_dev")
public class UserReview {
    @Id
    @Column(name = "review_id", nullable = false)
    private Long id;

    @Column(name = "from_user_id")
    private Long fromUserId;

    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "rating")
    private Integer rating;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

}