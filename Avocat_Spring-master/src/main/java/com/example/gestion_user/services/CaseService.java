package com.example.gestion_user.services;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Contributor;
import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.entities.enums.ContributorType;
import com.example.gestion_user.models.request.CaseDto;
import com.example.gestion_user.models.request.ContributorDto;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CaseService {

    Case addCase (CaseDto c) ;

    Case updateCase (Long idCase,CaseDto c) ;

    void deleteCase (Long idCase) ;
    List<Case> getCases() ;
    Case getCaseById (Long idCase);

   // public Case getCaseByTitle(String title);


    /*
    * Relation Trials and Case
    * */
    void addTrialToCase(Long case_id, Trial trial) ;
    List<Trial> getTrialsByCaseId(Long caseId);
    void deleteTrialFromCase(Long caseId, Long trialId);
    void updateTrial(Long caseId, Long trialId, Trial updatedTrial);


    void addContributorToCase(Long caseId, ContributorDto contributorDTO);
    List<Contributor> getContributorsByCaseId(Long caseId);
}
