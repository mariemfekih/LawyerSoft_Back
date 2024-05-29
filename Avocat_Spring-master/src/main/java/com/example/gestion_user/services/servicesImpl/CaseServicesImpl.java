package com.example.gestion_user.services.servicesImpl;
import com.example.gestion_user.entities.*;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.CaseDto;
import com.example.gestion_user.models.request.ContributorDto;
import com.example.gestion_user.repositories.*;

import com.example.gestion_user.services.CaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CaseServicesImpl implements CaseService {

    @Autowired
    CaseRepository caseRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TrialRepository trialRepository;
    @Autowired
    AuxiliaryRepository auxiliaryRepository;
    @Autowired
    CourtRepository courtRepository;
    @Autowired
    ContributorRepository contributorRepository;

    @Override
    public Case addCase(CaseDto c, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Case case1 = new Case(c.getTitle(), c.getDescription(), c.getCreationDate(), c.getClosingDate(), c.getType(),c.getState());
        case1.setUser(user);

        try {
            return caseRepository.save(case1);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create case: " + ex.getMessage());
        }
    }

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
        existingCase.setState(updatedCaseDto.getState());


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
    public List<Case> getCasesWithoutFolders() {
        return caseRepository.findByFolderIsNull();
    }
@Override
public List<Case> getUserCases(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    return user.getCases();
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
    public long getTotalCases() {
        return caseRepository.count();
    }
    @Override
    public long getTotalCasesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found !!"));
        List<Case> cases = caseRepository.findAllByUser(user);
        return cases.size();
    }
    //get total cases of a certain month :
    @Override
    public long getTotalCasesByUserMonth(Long userId) {
        return getTotalCasesByUserMonth(userId, LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()), LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
    }

    public long getTotalCasesByUserMonth(Long userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found !!"));
        Date startDateAsDate = localDateToDate(startDate);
        Date endDateAsDate = localDateToDate(endDate);
        List<Case> cases = caseRepository.findAllByUserAndCreationDateBetween(user, startDateAsDate, endDateAsDate);
        return cases.size();
    }

    public static Date localDateToDate(LocalDate localDate) {
        return new Date(ZonedDateTime.of(localDate, LocalTime.MIN, ZoneId.systemDefault()).toInstant().toEpochMilli());
    }


    @Override
    public double getPercentageChangeInTotalCasesByUser(Long userId) {
        LocalDate lastMonthStartDate = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastMonthEndDate = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        LocalDate currentMonthStartDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate currentMonthEndDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        long totalCasesLastMonth = getTotalCasesByUserMonth(userId, lastMonthStartDate, lastMonthEndDate);
        long totalCasesCurrentMonth = getTotalCasesByUserMonth(userId, currentMonthStartDate, currentMonthEndDate);

        if (totalCasesLastMonth == 0) {
            return 100.0; // If there were no cases last month, percentage change is 100%
        }

        double percentageChange = ((double) (totalCasesCurrentMonth - totalCasesLastMonth) / totalCasesLastMonth) * 100.0;
        return percentageChange;
    }
  /*  @Override
    public Case getCaseByTitle(String title) {
         return caseRepository.findByTitle(title);

    }*/

    //PARTIE DE CASE W TRIAL
/*

    public List<Trial> getTrialsByCaseId(Long caseId) {
        Optional<Case> optionalCase = caseRepository.findById(caseId);
        if (optionalCase.isPresent()) {
            Case caseInstance = optionalCase.get();
            return caseInstance.getTrials();
        } else {
            throw new EntityNotFoundException("Case not found with id: " + caseId);
        }
    }
    public void deleteTrialFromCase(Long caseId, Long trialId) {
        // Retrieve the case associated with the trial
        Case caseInstance = caseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + caseId));

        // Retrieve the trial to be deleted
        Trial trialToDelete = trialRepository.findById(trialId)
                .orElseThrow(() -> new EntityNotFoundException("Trial not found with id: " + trialId));

        // Verify if the trial belongs to the specified case
        if (!caseInstance.getTrials().contains(trialToDelete)) {
            throw new IllegalArgumentException("The specified trial does not belong to the specified case.");
        }

        // Remove the trial from the case's list of trials
        caseInstance.getTrials().remove(trialToDelete);

        // Save the updated case (without the trial)
        caseRepository.save(caseInstance);

        // Delete the trial entity (optional, depending on your data persistence strategy)
        trialRepository.delete(trialToDelete);
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
    }*/
//Contributor







    ////////////
    // CaseController.java

    @Override
    public void addContributorToCase(Long caseId, ContributorDto contributorDTO) {
        // Find the case by ID
        Case caseInstance = caseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + caseId));

        // Create a new contributor
        Contributor contributor = new Contributor();
        contributor.setType(contributorDTO.getType());
        contributor.setCases(caseInstance);

        // Set the auxiliary if provided
        if (contributorDTO.getIdAuxiliary() != null) {
            // Find auxiliary by ID
            Auxiliary auxiliary = auxiliaryRepository.findById(contributorDTO.getIdAuxiliary())
                    .orElseThrow(() -> new EntityNotFoundException("Auxiliary not found with id: " + contributorDTO.getIdAuxiliary()));
            contributor.setAuxiliary(auxiliary);
        }

        // Save the contributor
        contributorRepository.save(contributor);
    }

    @Override
    public List<Contributor> getContributorsByCaseId(Long caseId) {
        // Retrieve the case by ID
        Case caseInstance = caseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + caseId));

        // Return the list of contributors for the case
        return caseInstance.getContributors();
    }


    public void deleteContributorFromCase(Long caseId, Long contributorId) {
        Case caseInstance = caseRepository.findById(caseId)
                .orElseThrow(() -> new EntityNotFoundException("Case not found with id: " + caseId));

        Contributor contributorToDelete = contributorRepository.findById(contributorId)
                .orElseThrow(() -> new EntityNotFoundException("Contributor not found with id: " + contributorId));

        // Verify if the contributor belongs to the specified case
        if (!caseInstance.getContributors().contains(contributorToDelete)) {
            throw new IllegalArgumentException("The specified Contributor does not belong to the specified case.");
        }
        caseInstance.getContributors().remove(contributorToDelete);
        caseRepository.save(caseInstance);
        contributorRepository.delete(contributorToDelete);
    }



}
