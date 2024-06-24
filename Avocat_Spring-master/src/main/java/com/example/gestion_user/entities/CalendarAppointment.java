package com.example.gestion_user.entities;

import com.example.gestion_user.entities.enums.AppointmentType;
import com.example.gestion_user.entities.enums.LocationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calendarAppointments")
public class CalendarAppointment {    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(nullable = false, updatable = false)
private Long idAppointment ;

    @NotBlank(message = "title is required")
    @Column(nullable = false)
    private String title;

    //@Column(nullable = false)
    @Column
    private String description;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;


    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    @Column(unique = true)
    private String linkHangout;

    @Column(nullable = false)
    private LocationType location;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
