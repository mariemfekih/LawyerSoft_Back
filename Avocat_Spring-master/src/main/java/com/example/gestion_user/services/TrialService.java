package com.example.gestion_user.services;

import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.models.request.TrialDto;

import java.util.List;

public interface TrialService {
    Trial addTrial(Long case_id,Long court_id, Trial trial);
    Trial updateTrial(Long id, TrialDto updatedTrialDto);
    Trial updateTrial(Long caseId, Long courtId,Long trialId, Trial updatedTrial);
    void deleteTrial(Long id);
    List<Trial> getTrials();

    List<Trial> getTrialsByCaseId(Long caseId);

    Trial getTrialById(Long id);

    void deleteTrialFromCase(Long caseId, Long trialId);
    }
