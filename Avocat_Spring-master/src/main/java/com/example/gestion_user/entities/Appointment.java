package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.AppointmentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.api.client.util.DateTime;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long idAppointment ;

    @NotBlank(message = "title is required")
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    //@Column(nullable = false)
    private DateTime startTime;

   // @Column(nullable = false)
    private DateTime endTime;

    /*@Column(nullable = false)
    private DateTime startTime;

    @Column(nullable = false)
    private DateTime endTime;*/

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    @Column(nullable = false,unique = true)
    private String linkHangout;

    @Column(nullable = false)
    private String location;



@ManyToMany
private List<User> users;

    public List<User> getUsers() {
        if (users == null) {
            return new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    //Relation Appointment-Auxiliary
    @JsonIgnore
    @ManyToMany
    private List<Auxiliary> auxiliaries;


}
