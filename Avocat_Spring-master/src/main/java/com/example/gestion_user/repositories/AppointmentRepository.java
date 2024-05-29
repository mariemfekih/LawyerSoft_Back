package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> getAppointmentsByUsersOrderByDate(User user);
    //List<Appointment> findByAuxiliary(Auxiliary auxiliary);
//    List<Appointment> getAppointmentsByUsersOrderByAppointmentDate(User user);


}
