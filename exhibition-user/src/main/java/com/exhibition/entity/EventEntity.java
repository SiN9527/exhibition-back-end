package com.exhibition.entity;

import com.exhibition.entity.base.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "EVENT", schema = "ems_001")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEntity extends Auditable {

    @Id
    @Column(name = "event_id", length = 36, nullable = false)
    private String eventId; // UUID 格式的活動 ID

    @Column(name = "event_name", length = 255, nullable = false)
    private String eventName; // 活動名稱

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 活動描述

    @Column(name = "organizer", length = 255, nullable = false)
    private String organizer; // 主辦方

    @Column(name = "location", length = 255)
    private String location; // 活動地點

    @Column(name = "event_start_date", nullable = false)
    private Timestamp eventStartDate; // 活動開始時間

    @Column(name = "event_end_date", nullable = false)
    private Timestamp eventEndDate; // 活動結束時間

    @Column(name = "registration_start_date")
    private Timestamp registrationStartDate; // 報名開始時間

    @Column(name = "registration_end_date")
    private Timestamp registrationEndDate; // 報名截止時間

    @Column(name = "submission_start_date")
    private Timestamp submissionStartDate; // 投稿開始時間

    @Column(name = "submission_end_date")
    private Timestamp submissionEndDate; // 投稿截止時間

    @Column(name = "create_time", nullable = false, updatable = false)
    @Builder.Default
    private Timestamp createTime = Timestamp.from(Instant.now()); // 建立時間 (不可更新)

    @Column(name = "update_time", nullable = false)
    @Builder.Default
    private Timestamp updateTime = Timestamp.from(Instant.now()); // 更新時間 (可更新)




}

