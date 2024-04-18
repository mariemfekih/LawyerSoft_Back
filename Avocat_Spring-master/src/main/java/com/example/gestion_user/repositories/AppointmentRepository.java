package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.entities.Auxiliary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByAuxiliary(Auxiliary auxiliary);

}
