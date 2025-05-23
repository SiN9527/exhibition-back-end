package com.exhibition.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CommonCodePkEntity implements Serializable {
    private static final long serialVersionUID = -6632460949454906711L;
    @Size(max = 255)
    @NotNull
    @Column(name = "code_type", nullable = false)
    private String codeType;

    @Size(max = 255)
    @NotNull
    @Column(name = "code_option", nullable = false)
    private String codeOption;

    @NotNull
    @Column(name = "ind_codeseq", nullable = false)
    private Integer indCodeseq;

}