package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.CourtType;
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
    private Long id ;

    @Column(nullable = false)
    private String adress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourtType type;

    @Column(nullable = false)
    private String phone;

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
