package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.repositories.RdvRepository;
import com.example.gestion_user.services.RdvService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RdvServiceImpl implements RdvService {
    @Autowired
    RdvRepository rdvRepository;

    @Override
    public Appointment addRdv(Appointment rdv) {
        return rdvRepository.save(rdv);
    }

    @Override
    public List<Appointment> getRdvs() {
        return rdvRepository.findAll();
    }

    @Override
    public Appointment updateRdv(Appointment rdv) {
        return rdvRepository.save(rdv);
    }

    @Override
    public void deleteRdv(Integer idRdv) {
        rdvRepository.deleteById(idRdv);
    }

    @Override
    public Appointment getRdvById(Integer idRdv) {
        return rdvRepository.findById(idRdv).get();
    }
}
