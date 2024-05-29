package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.CalendarAppointment;
import com.example.gestion_user.models.request.CalendarAppointmentDto;
import com.example.gestion_user.services.CalendarAppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/CalendarApp")
public class CalendarAppointmentController {

    private final CalendarAppointmentService calendarAppointmentService;

    @PostMapping("/{userId}")
    public ResponseEntity<CalendarAppointment> addCalendarAppointment(@PathVariable Long userId, @RequestBody CalendarAppointmentDto dto) {
        CalendarAppointment appointment = calendarAppointmentService.addCalendarAppointment(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarAppointment> updateCalendarAppointment(@PathVariable Long id, @RequestBody CalendarAppointmentDto dto) {
        CalendarAppointment updatedAppointment = calendarAppointmentService.updateCalendarAppointment(id, dto);
        return ResponseEntity.ok().body(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendarAppointment(@PathVariable Long id) {
        calendarAppointmentService.deleteCalendarAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CalendarAppointment>> getCalendarAppointments() {
        List<CalendarAppointment> appointments = calendarAppointmentService.getCalendarAppointments();
        return ResponseEntity.ok().body(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarAppointment> getCalendarAppointmentById(@PathVariable Long id) {
        CalendarAppointment appointment = calendarAppointmentService.getCalendarAppointmentById(id);
        return ResponseEntity.ok().body(appointment);
    }
}
