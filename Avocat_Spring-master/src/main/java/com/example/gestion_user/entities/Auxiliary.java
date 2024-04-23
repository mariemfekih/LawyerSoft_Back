package com.example.gestion_user.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;

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
Auxiliary - appointment
 */
@OneToMany(mappedBy = "auxiliary", cascade = CascadeType.ALL)
private Set<Appointment> appointments;



    @OneToMany(mappedBy = "auxiliary", cascade = CascadeType.ALL)
    private Set<Contributor> contributors;

}
