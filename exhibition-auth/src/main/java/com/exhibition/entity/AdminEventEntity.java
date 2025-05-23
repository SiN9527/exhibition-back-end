package com.exhibition.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "ADMIN_EVENT", schema = "ems_001")
@Data
@Builder
@IdClass(AdminEventPkEntity.class)
@AllArgsConstructor
@NoArgsConstructor
public class AdminEventEntity {

    @Size(max = 50)
    @NotNull
    @Column(name = "account", nullable = false)
    @Id
    private String account;

    @Size(max = 50)
    @NotNull
    @Column(name = "event_id", nullable = false, length = 50)
    @Id
    private String eventId;

    @Column(name = "created_at")
    private Timestamp createdAt;

}