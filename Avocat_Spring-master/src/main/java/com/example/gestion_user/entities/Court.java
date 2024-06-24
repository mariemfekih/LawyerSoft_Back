package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.CourtType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courts")
public class Court implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long idCourt ;

    @Column(nullable = false)
    private String adress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourtType type;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String governorate;

    /*
 RELATION ENTRE COURT AND TRIAL
 */
    @OneToMany(mappedBy = "courtInstance", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trial> trials;

}
