package com.exhibition.entity;

import com.exhibition.entity.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "REGISTRATION_EXTRA", schema = "ems_001")
public class RegistrationExtraEntity  extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "extra_id", nullable = false)
    private Long id;


    @Column(name = "registration_id", nullable = false, insertable = false, updatable = false)
    private String registrationId;


    @Column(name = "attend_gala_dinner")
    private boolean attendGalaDinner;


    @Column(name = "attend_tour")
    private boolean attendTour;

    @Column(name = "attend_welcome_reception")
    private boolean attendWelcomeReception;

    @Column(name = "is_vegetarian")
    private boolean isVegetarian;

    @Column(name = "fee", nullable = false)
    private Integer fee;


    @OneToOne
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id")
    private RegistrationMainEntity registrationMain;
}