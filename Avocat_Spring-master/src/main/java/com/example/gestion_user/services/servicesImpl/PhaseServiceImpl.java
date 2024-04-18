package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Phase;
import com.example.gestion_user.repositories.PhaseRepository;
import com.example.gestion_user.services.PhaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PhaseServiceImpl implements PhaseService {
    @Autowired
    PhaseRepository phaserepository;

    @Override
    public Phase addPhase(Phase phase) {
       return phaserepository.save(phase);
    }

    @Override
    public List<Phase> getPhases() {
        return phaserepository.findAll();
    }

    @Override
    public Phase updatePhase(Phase phase) {
        return phaserepository.save(phase);
    }

    @Override
    public void deletePhase(Integer idPhase) {
        phaserepository.deleteById(idPhase);
    }

    @Override
    public Phase getPhaseById(Integer idPhase) {
        return phaserepository.findById(idPhase).get();   }
    @Override
    public Phase getPhaseByTitle(String title) {
        return phaserepository.getPhaseByTitle(title);
    }

}
