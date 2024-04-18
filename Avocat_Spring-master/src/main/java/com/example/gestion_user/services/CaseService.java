package com.example.gestion_user.services;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Contributor;
import com.example.gestion_user.entities.Trial;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CaseService {

    Case addCase (Case c) ;

    //public Affaire addAffaire(Affaire affaire, Integer idTribunal);

    List<Case> getCases() ;

    Case updateCase (Case c) ;

    void deleteCase (Integer idCase) ;

    Case getCaseById (Integer idCase);

    public Case getCaseByTitle(String title);


    /*
    * Relation Trials and Case
    * */
    void addTrialToCase(Integer case_id, Trial trial) ;
    List<Trial> getTrialsByCaseId(Integer caseId);
    void deleteTrialFromCase(Integer caseId, Integer trialId);
    void updateTrial(Integer caseId, Integer trialId, Trial updatedTrial);

    @Transactional
        // Ensures database consistency across related operations
    Case addContributorToCase(Integer caseId, Contributor contributor) throws EntityNotFoundException;
}
