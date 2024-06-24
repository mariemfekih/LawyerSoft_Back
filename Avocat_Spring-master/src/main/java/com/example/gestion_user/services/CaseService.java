package com.example.gestion_user.services;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Contributor;
import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.entities.enums.ContributorType;
import com.example.gestion_user.models.request.CaseDto;
import com.example.gestion_user.models.request.ContributorDto;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

public interface CaseService {
   Case addCase(CaseDto c, Long userId,Long customerId);
  //  Case addCase(CaseDto c, Long userId);
    Case updateCase (Long idCase,CaseDto c) ;

    void deleteCase (Long idCase) ;
    List<Case> getCases() ;
    Case getCaseById (Long idCase);
  List<Case> getUserCases(Long userId);
  List<Case> getUserCasesWithoutFolder(Long userId);
   // public Case getCaseByTitle(String title);


  long getTotalCases();

  long getTotalCasesByUser(Long userId);
  long getTotalCasesByUserMonth(Long userId);
 // long getTotalCasesByUserMonth3arg(Long userId, LocalDate startDate, LocalDate endDate);



  double getPercentageChangeInTotalCasesByUser(Long userId);

  /*
    * Relation Trials and Case
    * */
    //void addTrialToCase(Long case_id, Trial trial) ;
/*    List<Trial> getTrialsByCaseId(Long caseId);
    void deleteTrialFromCase(Long caseId, Long trialId);
    void updateTrial(Long caseId, Long trialId, Trial updatedTrial);*/

    List<Case> getCasesWithoutFolders();

    void addContributorToCase(Long caseId, ContributorDto contributorDTO);
  void updateContributor(Long contributorId, ContributorDto contributorDTO);
  List<Contributor> getContributorsByCaseId(Long caseId);
    void deleteContributorFromCase(Long caseId,Long contributorId);
  Contributor getContributorById(Long id);
}
