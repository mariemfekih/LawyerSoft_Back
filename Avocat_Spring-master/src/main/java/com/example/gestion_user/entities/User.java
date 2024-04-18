package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false, unique=true , length = 8)
    private Long cin;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique=true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    //private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Boolean gender;

    private String profileImage;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

    /*
     RELATION ENTRE User AND Fee
     */
    @OneToMany(mappedBy = "userInstance", cascade = CascadeType.ALL)
    private List<Fee> fees;
    /*
    Relation entre User et Case
    */
    @ManyToMany(mappedBy="users")
    private List<Case> cases ;
    /*
    Relation entre User et Privilege
    */
    @ManyToMany(mappedBy="users")
    private List<Privilege> privileges ;


/*
user-appointment
 */
    @OneToMany(mappedBy = "user")
    private Set<Appointment> appointments;


}
