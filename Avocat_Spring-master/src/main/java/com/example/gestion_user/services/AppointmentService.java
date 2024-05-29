
package com.example.gestion_user.services;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.entities.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment createAppointment(Appointment appointment) throws MessagingException, IOException, GeneralSecurityException;

    Optional<Appointment> getAppointment(Long id);

    List<Appointment> getAllAppointment();

    Appointment updateAppointment(Appointment appointment);

    void deleteAppointment(Long id);

    List<Appointment> getAppointmentsByUser(User user);
}