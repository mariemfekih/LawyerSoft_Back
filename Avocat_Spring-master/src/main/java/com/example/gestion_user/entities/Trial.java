package com.example.gestion_user.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Trial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTrial", nullable = false, updatable = false)
    private Integer idTrial;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String judgement;

    /*
    RELATION ENTRE Case AND Trial
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id") // foreign key column name
    private Case caseInstance;

    /*
   RELATION ENTRE Court AND Trial
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id") // foreign key column name
    private Case courtInstance;



}
