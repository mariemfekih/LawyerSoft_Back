package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.ContributorType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contributors")
public class Contributor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long idContributor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContributorType type;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case cases;

    @ManyToOne
    @JoinColumn(name = "auxiliary_id")
    private Auxiliary auxiliary;
}
