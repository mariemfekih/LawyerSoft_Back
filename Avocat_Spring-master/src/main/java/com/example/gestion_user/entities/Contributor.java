package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.ContributorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contributor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer idContributor;

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
