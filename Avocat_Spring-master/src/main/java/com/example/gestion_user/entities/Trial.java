package com.example.gestion_user.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trials")
public class Trial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( nullable = false, updatable = false)
    private Long idTrial;

    @NotBlank(message = "title is requires")
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
