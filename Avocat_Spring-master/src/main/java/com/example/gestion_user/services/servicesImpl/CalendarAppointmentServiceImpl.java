package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.CalendarAppointment;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Fee;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.CalendarAppointmentDto;
import com.example.gestion_user.repositories.CalendarAppointmentRepository;
import com.example.gestion_user.repositories.UserRepository;
import com.example.gestion_user.services.CalendarAppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CalendarAppointmentServiceImpl implements CalendarAppointmentService {
    @Autowired
    private CalendarAppointmentRepository calendarAppointmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public CalendarAppointment addCalendarAppointment(Long UserId,CalendarAppointmentDto c) {
        User userInstance = userRepository.findById(UserId)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + UserId));

        CalendarAppointment calendarAppointment = new CalendarAppointment();
            calendarAppointment.setTitle(c.getTitle());
            calendarAppointment.setDescription(c.getDescription());
            calendarAppointment.setStartTime(c.getStartTime());
            calendarAppointment.setEndTime(c.getEndTime());
            calendarAppointment.setDate(c.getDate());
            calendarAppointment.setType(c.getType());
            calendarAppointment.setLinkHangout(c.getLinkHangout());
            calendarAppointment.setLocation(c.getLocation());
            calendarAppointment.setUser(userInstance);

        try {
            return calendarAppointmentRepository.save(calendarAppointment);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create appointment: " + ex.getMessage());
        }    }

    @Override
    public CalendarAppointment updateCalendarAppointment(Long id, CalendarAppointmentDto updatedCalendarAppointmentDto) {
        CalendarAppointment existingCalendarAppointment = calendarAppointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("appointment not found with id: " + id));

        // Update the fields of the existing Fee entity with values from the DTO
        existingCalendarAppointment.setTitle(updatedCalendarAppointmentDto.getTitle());
        existingCalendarAppointment.setDescription(updatedCalendarAppointmentDto.getDescription());
        existingCalendarAppointment.setStartTime(updatedCalendarAppointmentDto.getStartTime());
        existingCalendarAppointment.setEndTime(updatedCalendarAppointmentDto.getEndTime());
        existingCalendarAppointment.setDate(updatedCalendarAppointmentDto.getDate());
        existingCalendarAppointment.setType(updatedCalendarAppointmentDto.getType());
        existingCalendarAppointment.setLinkHangout(updatedCalendarAppointmentDto.getLinkHangout());
        existingCalendarAppointment.setLocation(updatedCalendarAppointmentDto.getLocation());

        // Save the updated Fee entity
        try {
            return calendarAppointmentRepository.save(existingCalendarAppointment);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update appointment with id: " + id + ". " + ex.getMessage());
        }    }

    @Override
    public void deleteCalendarAppointment(Long id) {
        calendarAppointmentRepository.deleteById(id);

    }

    @Override
    public List<CalendarAppointment> getCalendarAppointments() {
        return calendarAppointmentRepository.findAll();
    }

    @Override
    public CalendarAppointment getCalendarAppointmentById(Long id) {
        return calendarAppointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));
    }



}
