package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.CourtType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Court implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCourt",nullable = false, updatable = false)
    private Integer idCourt ;

    @Column(nullable = false)
    private String adress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourtType type;

    @Column(nullable = false)
    private Long phone;

    @Column(nullable = false)
    private String governorate;

    @Column(nullable = false)
    private String municipality;

    /*
 RELATION ENTRE COURT AND TRIAL
 */
    @OneToMany(mappedBy = "courtInstance", cascade = CascadeType.ALL)
    private List<Trial> trials;

}
