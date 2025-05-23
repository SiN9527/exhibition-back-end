package com.exhibition.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "community_comment", schema = "hititoff_dev")
public class CommunityComment {
    @Id
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}