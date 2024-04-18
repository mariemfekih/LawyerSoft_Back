package com.example.gestion_user.services;

import com.example.gestion_user.entities.Trial;

import java.util.List;

public interface TrialService {
    Trial addTrial(Trial trial);

    List<Trial> getTrials();

    Trial updateTrial(Trial trial);

    void deleteTrial(Integer idAudience);

    Trial getTrialById(Integer idAudience);


    }
