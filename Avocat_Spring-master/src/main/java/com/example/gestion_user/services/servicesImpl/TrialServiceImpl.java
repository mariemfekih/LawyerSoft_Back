package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.repositories.TrialRepository;
import com.example.gestion_user.services.TrialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrialServiceImpl implements TrialService {
    @Autowired
    TrialRepository trialRepository;

    @Override
    public Trial addTrial(Trial trial) {
        return trialRepository.save(trial);
    }

    @Override
    public List<Trial> getTrials() {
        return trialRepository.findAll();
    }

    @Override
    public Trial updateTrial(Trial trial) {
        return trialRepository.save(trial);
    }

    @Override
    public void deleteTrial(Long id) {
        trialRepository.deleteById(id);
    }

    @Override
    public Trial getTrialById(Long id) {
        return trialRepository.findById(id).get();
    }

}
