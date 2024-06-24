package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.DateTime;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")

public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique=true , length = 8)
    private String cin;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    //private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Boolean gender;

    //private String profileImage;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    @Column
    private Date joinDate;
    private String[] authorities;
    private boolean isActive = false;
    private boolean isNotLocked;
    private Long lawyerId;

   /* @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;
*/
    @Column
    private String profileImage;


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
     RELATION ENTRE User AND Fee
     */
    @OneToMany(mappedBy = "userInstance", cascade = CascadeType.ALL)
    //@JsonIgnore
    private List<Fee> fees;
    /*
    Relation entre User et Case
    */
   @OneToMany(mappedBy = "user")
   @JsonIgnore
    private List<Case> cases;


    /*
    Relation entre User et Privilege
    */

    @ManyToMany(mappedBy="users")
    @JsonIgnore
    private List<Privilege> privileges ;


/*
user-appointment
 */
/*
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Appointment> appointments;
*/

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Auxiliary> auxiliaries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Customer> customers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CalendarAppointment> calendarAppointments;
}
