package com.example.gestion_user.services;

import com.example.gestion_user.entities.CalendarAppointment;
import com.example.gestion_user.models.request.CalendarAppointmentDto;

import java.util.List;

public interface CalendarAppointmentService {
    CalendarAppointment addCalendarAppointment(Long id,CalendarAppointmentDto a) ;
    CalendarAppointment updateCalendarAppointment (Long id , CalendarAppointmentDto a) ;
    void deleteCalendarAppointment (Long id) ;
    List<CalendarAppointment> getCalendarAppointments() ;
    CalendarAppointment getCalendarAppointmentById (Long id);
}
