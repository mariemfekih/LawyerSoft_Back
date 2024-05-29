package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.services.AppointmentService;
import com.example.gestion_user.services.UserService;
import com.example.gestion_user.services.servicesImpl.AppointmentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/Appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;


    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment a) throws MessagingException, IOException, GeneralSecurityException {
        return appointmentService.createAppointment(a);
    }


    @GetMapping("/{appointmentId}")
    public Optional<Appointment> getAppointment(@PathVariable("appointmentId") Long appointmentId) {

        return appointmentService.getAppointment(appointmentId);
    }


    @GetMapping
    public List<Appointment> getAllAppointment() {

        return appointmentService.getAllAppointment();
    }

    @PutMapping("/{appointmentId}")
    public Appointment updateAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody Appointment appointment) {

        appointment.setIdAppointment(appointmentId);
        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{appointmentId}")
    public void deleteAppointment(@PathVariable("appointmentId") Long appointmentId) {

        appointmentService.deleteAppointment(appointmentId);
    }


    @GetMapping("/{idUser}")
    public List<Appointment> getAppointmentsByUser(@PathVariable("userId") Long idUser){
        Optional<User> user = userService.getUserByid(idUser);
        return appointmentService.getAppointmentsByUser(user.get());
    }


/*    @GetMapping("/generateExcel")
    public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        ArrayList<Appointment> listOfStudents = (ArrayList<Appointment>) appointmentService.getAllAppointment();
        AppointmentServiceImpl generator = new AppointmentServiceImpl(listOfStudents);
        generator.generateExcelFile(response);
    }*/
}
