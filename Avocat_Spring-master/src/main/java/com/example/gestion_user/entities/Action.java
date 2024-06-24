package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.FeeType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "actions")
public class Action implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long idAction ;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String reference ;

    @Column(nullable = false)
    private String description ;

    @Column(nullable = false)
    private float amount ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_id") // foreign key column name
    private Fee feeInstance;


}
