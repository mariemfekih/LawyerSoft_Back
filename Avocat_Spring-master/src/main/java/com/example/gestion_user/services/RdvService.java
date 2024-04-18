package com.example.gestion_user.services;

import com.example.gestion_user.entities.Appointment;

import java.util.List;

public interface RdvService {
    Appointment addRdv(Appointment rdv);

    List<Appointment> getRdvs();

    Appointment updateRdv(Appointment rdv);

    void deleteRdv(Integer idRdv);

    Appointment getRdvById(Integer idRdv);
}
