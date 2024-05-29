package com.example.gestion_user.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auxiliaries")
public class Auxiliary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long idAuxiliary;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String cin;

    @Column(nullable = false ,unique = true)
    private String email;

    @Column(nullable = false)
    private String job;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String city;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

/*
* Controle de sais
* */
public void setCin(String cin) {
    if (cin != null && cin.length() == 8 && (cin.startsWith("0") || cin.startsWith("1"))) {
        this.cin = cin;
    } else {
        throw new IllegalArgumentException("CIN must be exactly 8 characters long and start with 0 or 1");
    }
}

    /*
Auxiliary - user
 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;



    @OneToMany(mappedBy = "auxiliary", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Contributor> contributors;

}
