package com.exhibition.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "community_post", schema = "hititoff_dev")
public class CommunityPost {
    @Id
    @Column(name = "post_id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title", length = 100)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}