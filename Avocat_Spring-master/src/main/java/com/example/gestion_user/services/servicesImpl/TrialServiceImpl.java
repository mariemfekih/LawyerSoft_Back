package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Contributor;
import com.example.gestion_user.entities.Court;
import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.CaseDto;
import com.example.gestion_user.models.request.TrialDto;
import com.example.gestion_user.repositories.CaseRepository;
import com.example.gestion_user.repositories.CourtRepository;
import com.example.gestion_user.repositories.TrialRepository;
import com.example.gestion_user.services.TrialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class TrialServiceImpl implements TrialService {
    @Autowired
    TrialRepository trialRepository;
    @Autowired
    CaseRepository caseRepository;
    @Autowired
    CourtRepository courtRepository;

    public Trial addTrial(Long case_id,Long court_id, Trial trial) {
        Optional<Case> optionalCase = caseRepository.findById(case_id);
        Optional<Court> optionalCourt = courtRepository.findById(court_id);

        if (optionalCase.isPresent() && optionalCourt.isPresent()) {
            Case caseInstance = optionalCase.get();
            Court courtInstance = optionalCourt.get();
            trial.setCaseInstance(caseInstance); // Associate the trial with the case
            trial.setCourtInstance(courtInstance);
            caseInstance.getTrials().add(trial); // Add the trial to the case's list of trials
            courtInstance.getTrials().add(trial);
            // Save both the case and the trial entities
            caseRepository.save(caseInstance);
            courtRepository.save(courtInstance);
            return  trialRepository.save(trial);
        } else {
            throw new EntityNotFoundException("Case not found with id: " + case_id);
        }
    }
    @Override
    public Trial updateTrial(Long id, TrialDto updatedTrialDto) {
        // Find the existing Trial entity by ID
        Trial existingTrial = trialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trial not found with id: " + id));

        // Update the fields of the existing Trial entity with values from the DTO
        existingTrial.setTitle(updatedTrialDto.getTitle());
        existingTrial.setDescription(updatedTrialDto.getDescription());
        existingTrial.setJudgement(updatedTrialDto.getJudgement());

        if (updatedTrialDto.getCourtInstance()!= null && updatedTrialDto.getCourtInstance().getIdCourt()!= null) {
            Court courtInstance = courtRepository.findById(updatedTrialDto.getCourtInstance().getIdCourt())
                    .orElseThrow(() -> new EntityNotFoundException("Court not found with id: " + updatedTrialDto.getCourtInstance().getIdCourt()));
            existingTrial.setCourtInstance(courtInstance);
        }

        // Save the updated Trial entity
        try {
            return trialRepository.save(existingTrial);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update trial with id: " + id + ". " + ex.getMessage());
        }
    }

    @Override
    public void deleteTrial(Long id) {
        trialRepository.deleteById(id);
    }
    @Override
    public List<Trial> getTrials() {
        return trialRepository.findAll();
    }
    @Override
    public List<Trial> getTrialsByCaseId(Long caseId) {
        Optional<Case> optionalCase = caseRepository.findById(caseId);
        if (optionalCase.isPresent()) {
            Case caseInstance = optionalCase.get();
            return caseInstance.getTrials();
        } else {
            throw new EntityNotFoundException("Case not found with id: " + caseId);
        }
    }


    @Override
    public Trial getTrialById(Long id) {
        return trialRepository.findById(id).get();
    }


    public Trial updateTrial(Long caseId, Long courtId,Long trialId, Trial updatedTrial) {
        Case caseInstance = caseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Affaire non trouvée avec l'identifiant : " + caseId));
        Court courtInstance = courtRepository.findById(courtId)
                .orElseThrow(() -> new EntityNotFoundException("Tribunal non trouvée avec l'identifiant : " + caseId));
        Trial trialToUpdate = trialRepository.findById(trialId)
                .orElseThrow(() -> new EntityNotFoundException("Essai non trouvé avec l'identifiant : " + trialId));

        if (!caseInstance.getTrials().contains(trialToUpdate) && !courtInstance.getTrials().contains(trialToUpdate)) {
            throw new IllegalArgumentException("L'essai spécifié n'appartient pas à l'affaire/tribunal spécifiée.");
        }

        // Mettre à jour les détails de l'essai
        trialToUpdate.setTitle(updatedTrial.getTitle());
        trialToUpdate.setDescription(updatedTrial.getDescription());
        trialToUpdate.setJudgement(updatedTrial.getJudgement());
        // Mettre à jour d'autres champs de l'essai si nécessaire

        // Sauvegarder les modifications de l'essai dans la base de données
       return  trialRepository.save(trialToUpdate);
    }

    @Override
    public void deleteTrialFromCase(Long caseId, Long trialId) {
        Case caseInstance = caseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + caseId));

        Trial trialToDelete = trialRepository.findById(trialId)
                .orElseThrow(() -> new EntityNotFoundException("Trial not found with id: " + trialId));

        // Verify if the trial belongs to the specified case
        if (!caseInstance.getTrials().contains(trialToDelete)) {
            throw new IllegalArgumentException("The specified Trial does not belong to the specified case.");
        }

        caseInstance.getTrials().remove(trialToDelete);
        caseRepository.save(caseInstance);
        trialRepository.delete(trialToDelete);
    }

}
