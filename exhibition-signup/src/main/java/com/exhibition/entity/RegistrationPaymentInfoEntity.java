package com.exhibition.entity;

import com.exhibition.entity.base.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "RegistrationPaymentInfoEntity")
@Table(name = "REGISTRATION_PAYMENT_INFO", schema = "ems_001")
public class RegistrationPaymentInfoEntity  extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_info_id", nullable = false)
    private Long id;

    @Size(max = 36)
    @NotNull
    @Column(name = "registration_id", nullable = false, length = 36, insertable = false, updatable = false)
    private String registrationId;

    @NotNull
    @Lob
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @NotNull
    @ColumnDefault("'UNPAID'")
    @Lob
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @NotNull
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @ColumnDefault("0")
    @Column(name = "receipt_required")
    private Boolean receiptRequired;

    @Size(max = 100)
    @Column(name = "receipt_title", length = 100)
    private String receiptTitle;

    @Size(max = 30)
    @Column(name = "vat_number", length = 30)
    private String vatNumber;

    @Size(max = 4)
    @Column(name = "report_year", length = 4)
    private String reportYear;

    @Size(max = 255)
    @Column(name = "mailing_address")
    private String mailingAddress;

    @Column(name = "is_group")
    private boolean isGroup;


    @OneToOne
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id")
    private RegistrationMainEntity registrationMain;
}