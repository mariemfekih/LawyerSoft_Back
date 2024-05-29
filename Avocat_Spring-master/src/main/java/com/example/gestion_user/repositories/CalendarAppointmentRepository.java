package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.CalendarAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarAppointmentRepository extends JpaRepository<CalendarAppointment, Long> {
}
