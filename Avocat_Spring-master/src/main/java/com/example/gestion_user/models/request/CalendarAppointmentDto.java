package com.example.gestion_user.models.request;

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

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CalendarAppointmentDto {

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private AppointmentType type;

    private String linkHangout;

    private LocationType location;



}
