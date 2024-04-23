package com.example.gestion_user.services.servicesImpl;
import com.example.gestion_user.entities.Contributor;
import com.example.gestion_user.entities.enums.ContributorType;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.CaseDto;
import com.example.gestion_user.repositories.ContributorRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.repositories.CaseRepository;
import com.example.gestion_user.repositories.CourtRepository;
import com.example.gestion_user.repositories.TrialRepository;
import com.example.gestion_user.services.CaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CaseServicesImpl implements CaseService {

    @Autowired
    CaseRepository caseRepository;
    @Autowired
    TrialRepository trialRepository;

    @Autowired
    CourtRepository courtRepository;
    @Autowired
    ContributorRepository contributorRepository;

   @Override
    public Case addCase(CaseDto c) {
        Case case1= new Case();
        case1.setTitle(c.getTitle());
        case1.setDescription(c.getDescription());
        case1.setCreationDate(c.getCreationDate());
        case1.setClosingDate(c.getClosingDate());
        case1.setType(c.getType());
       try {
           return caseRepository.save(case1);
       } catch (Exception ex) {
           throw new NotFoundException("Failed to create case: " + ex.getMessage());
       }    }
    @Override
    public Case updateCase(Long id, CaseDto updatedCaseDto) {
        // Find the existing Case entity by ID
        Case existingCase = caseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + id));

        // Update the fields of the existing Case entity with values from the DTO
        existingCase.setTitle(updatedCaseDto.getTitle());
        existingCase.setDescription(updatedCaseDto.getDescription());
        existingCase.setCreationDate(updatedCaseDto.getCreationDate());
        existingCase.setClosingDate(updatedCaseDto.getClosingDate());
        existingCase.setType(updatedCaseDto.getType());

        // Save the updated Case entity
        try {
            return caseRepository.save(existingCase);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update case with id: " + id + ". " + ex.getMessage());
        }
    }

    @Override
    public List<Case> getCases() {
        return caseRepository.findAll();
    }



    @Override
    public void deleteCase(Long idCase) {
        caseRepository.deleteById(idCase);
    }

    @Override
    public Case getCaseById(Long idCase) {
        return caseRepository.findById(idCase).get() ;
    }


    @Override
    public Case getCaseByTitle(String title) {
         return caseRepository.findByTitle(title);

    }

    //PARTIE MTAA CASE W TRIAL
    public void addTrialToCase(Long case_id, Trial trial) {
        Optional<Case> optionalCase = caseRepository.findById(case_id);
        if (optionalCase.isPresent()) {
            Case caseInstance = optionalCase.get();
            trial.setCaseInstance(caseInstance); // Associate the trial with the case
            caseInstance.getTrials().add(trial); // Add the trial to the case's list of trials
            caseRepository.save(caseInstance); // Save the changes to the case (including the new trial)
        } else {
            throw new EntityNotFoundException("Case not found with id: " + case_id);
        }
    }
    public List<Trial> getTrialsByCaseId(Long caseId) {
        Optional<Case> optionalCase = caseRepository.findById(caseId);
        if (optionalCase.isPresent()) {
            Case caseInstance = optionalCase.get();
            return caseInstance.getTrials();
        } else {
            throw new EntityNotFoundException("Case not found with id: " + caseId);
        }
    }
    @Transactional
    public void deleteTrialFromCase(Long caseId, Long trialId) {
        // Retrieve the case from the database
        Case foundCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + caseId));

        // Retrieve the trial from the case's trials list
        Trial trialToRemove = foundCase.getTrials().stream()
                .filter(trial -> trial.getId().equals(trialId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Trial not found with id: " + trialId));

        // Remove the trial from the case's trials list
        foundCase.getTrials().remove(trialToRemove);

        // Save the updated case (no need for separate check on removal here)
        caseRepository.save(foundCase);
    }


    public void updateTrial(Long caseId, Long trialId, Trial updatedTrial) {
        // Récupérer l'affaire associée à l'essai
        Case caseInstance = caseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Affaire non trouvée avec l'identifiant : " + caseId));

        // Récupérer l'essai à mettre à jour
        Trial trialToUpdate = trialRepository.findById(trialId)
                .orElseThrow(() -> new EntityNotFoundException("Essai non trouvé avec l'identifiant : " + trialId));

        // Vérifier si l'essai appartient à l'affaire spécifiée
        if (!caseInstance.getTrials().contains(trialToUpdate)) {
            throw new IllegalArgumentException("L'essai spécifié n'appartient pas à l'affaire spécifiée.");
        }

        // Mettre à jour les détails de l'essai
        trialToUpdate.setTitle(updatedTrial.getTitle());
        trialToUpdate.setDescription(updatedTrial.getDescription());
        trialToUpdate.setJudgement(updatedTrial.getJudgement());
        // Mettre à jour d'autres champs de l'essai si nécessaire

        // Sauvegarder les modifications de l'essai dans la base de données
        trialRepository.save(trialToUpdate);
    }
//Contributor







    ////////////
    @Transactional // Ensures database consistency across related operations
    public Case addContributorToCase(Long caseId, Contributor contributor) throws EntityNotFoundException {
        Optional<Case> optionalCase = caseRepository.findById(caseId);
        if (!optionalCase.isPresent()) {
            throw new EntityNotFoundException("Case not found with id: " + caseId);
        }
        Case existingCase = optionalCase.get();
        existingCase.getContributors().add(contributor); // Assuming 'contributors' is a Set or List in Case
        Case updatedCase = caseRepository.save(existingCase); // Save the updated case with the new contributor
        return updatedCase;
    }

}
