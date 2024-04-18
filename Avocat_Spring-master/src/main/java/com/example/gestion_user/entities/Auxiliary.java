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
public class Auxiliary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer idAuxiliary;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private Long cin;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String job;

    @Column(nullable = false)
    private Long phone;

    @Column(nullable = false)
    private String city;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthDate;


/*
Auxiliary - appointment
 */
@OneToMany(mappedBy = "auxiliary", cascade = CascadeType.ALL)
private Set<Appointment> appointments;



    @OneToMany(mappedBy = "auxiliary", cascade = CascadeType.ALL)
    private Set<Contributor> contributors;

}
