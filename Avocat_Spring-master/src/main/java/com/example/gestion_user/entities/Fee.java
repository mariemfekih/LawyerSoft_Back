package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.FeeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Fee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFee",nullable = false, updatable = false)
    private Integer idFee ;

    @Column(nullable = false, unique = true)
    private String reference ;

    @Column(nullable = false)
    private float amount ;

    @Column(nullable = false)
    private LocalDate date ;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeeType type ;

    @Column(nullable = false)
    private String remain ;

    /*
       RELATION ENTRE User AND FEE
        */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // foreign key column name
    private Case userInstance;


}
