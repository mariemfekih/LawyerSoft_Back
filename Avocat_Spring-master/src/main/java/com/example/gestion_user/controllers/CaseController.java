package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Contributor;  // Added import statement for Contributor
import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.entities.enums.ContributorType;
import com.example.gestion_user.entities.report.Report;
import com.example.gestion_user.entities.report.StringResult;
import com.example.gestion_user.models.request.CaseDto;
import com.example.gestion_user.models.request.ContributorDto;
import com.example.gestion_user.services.CaseService;
import com.example.gestion_user.services.JasperReportService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
@RestController
@RequestMapping("/Case")
@CrossOrigin("http://localhost:4200")

public class CaseController {

    @Autowired
    CaseService caseService;
    @Autowired
    JasperReportService jasperReportService;
    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);
    @PostMapping("/user/{userId}/customer/{customerId}")
    public ResponseEntity<Case> addCase(@RequestBody CaseDto c, @PathVariable Long userId, @PathVariable Long customerId) {
        Case addedCase = caseService.addCase(c, userId, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCase);
    }

/*@PostMapping("/{userId}")
public ResponseEntity<Case> addCase(@RequestBody CaseDto c, @PathVariable Long userId) {
    Case addedCase = caseService.addCase(c, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(addedCase);
}*/

    /*
     * Update Case
     */
    @PutMapping("/{idCase}")
    public ResponseEntity<Case> updateCase(@PathVariable Long idCase, @RequestBody CaseDto updatedCaseDto) {
        Case existingCase = caseService.getCaseById(idCase);

        if (existingCase == null) {
            // If the Case doesn't exist, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
        Case updatedCase = caseService.updateCase(idCase, updatedCaseDto);
        // Return the updated case with a 200 OK status
        return ResponseEntity.ok(updatedCase);
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idCase}")
    public ResponseEntity<Void> deleteCase(@PathVariable("idCase") Long idCase) {
        caseService.deleteCase(idCase);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<Case>> getCases() {
        List<Case> listCases = caseService.getCases();
        return ResponseEntity.ok().body(listCases);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Case>> getUserCases(@PathVariable Long userId) {
        List<Case> cases = caseService.getUserCases(userId);
        return ResponseEntity.ok(cases);
    }
    @GetMapping("/user/{userId}/without-folder")
    public ResponseEntity<List<Case>> getUserCasesWithoutFolder(@PathVariable Long userId) {
        List<Case> cases = caseService.getUserCasesWithoutFolder(userId);
        return ResponseEntity.ok(cases);
    }
    @GetMapping("/{idCase}")
    public ResponseEntity<Case> getCaseById(@PathVariable("idCase") Long idCase) {
        Case caseById = caseService.getCaseById(idCase);
        if (caseById != null) {
            return ResponseEntity.ok().body(caseById);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/withoutFolders")
    public List<Case> getCasesWithoutFolders() {
        return caseService.getCasesWithoutFolders();
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalCases() {
        long totalCases = caseService.getTotalCases();
        return new ResponseEntity<>(totalCases, HttpStatus.OK);
    }
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Long> getTotalCasesByUser(@PathVariable Long userId) {
        Long totalCases = caseService.getTotalCasesByUser(userId);
        return new ResponseEntity<>(totalCases, HttpStatus.OK);
    }
    @GetMapping("/user/{userId}/total-month")
    public ResponseEntity<Long> getTotalCasesByUserMonth(@PathVariable Long userId) {
        Long totalCases = caseService.getTotalCasesByUserMonth(userId);
        return new ResponseEntity<>(totalCases, HttpStatus.OK);
    }
    @GetMapping("/user/{userId}/percentage-change")
    public ResponseEntity<Double> getPercentageChangeInTotalCasesByUser(@PathVariable Long userId) {
        double percentageChange = caseService.getPercentageChangeInTotalCasesByUser(userId);
        return ResponseEntity.ok(percentageChange);
    }
    @GetMapping("/reports")
    public ResponseEntity<String> generateReport() {
        try {
            List<Case> cases = caseService.getCases(); // Retrieve all cases
            jasperReportService.generateReport(cases);
            return ResponseEntity.ok().body("Report generated successfully!");
        } catch (JRException e) {
            logger.error("Error generating report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate report.");
        }
    }
    @PostMapping("/pdf")
    public String generatePdf(@RequestBody Map<String, Object> params) {
        return jasperReportService.generatePdf(params);
    }




  /*  @GetMapping("/title/{title}")
    public ResponseEntity<Case> getCaseByTitle(@PathVariable("title") String title) {
        Case casetitle = caseService.getCaseByTitle(title);
        if (casetitle != null) {
            return ResponseEntity.ok().body(casetitle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/


    ///////////////Add trial to case:
//    @PostMapping("/{case_id}/addTrial")
//    public ResponseEntity<?> addTrialToCase(@PathVariable Long case_id, @RequestBody Trial trial) {
//        try {
//            caseService.addTrialToCase(case_id, trial);
//            logger.info("trial added");
//            return ResponseEntity.ok().build();
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding trial to case: " + e.getMessage());
//        }
//    }
  /*  @PutMapping("/{caseId}/updateTrial/{trialId}")
    public ResponseEntity<Trial> updateTrial(@PathVariable Long caseId, @PathVariable Long trialId, @RequestBody Trial updatedTrial) {
        try {
            caseService.updateTrial(caseId, trialId, updatedTrial);
            logger.info("trial"+trialId+" updated");
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{caseId}/deleteTrial/{trialId}")
    public ResponseEntity<?> deleteTrial(@PathVariable Long caseId, @PathVariable Long trialId) {
        try {
            caseService.deleteTrialFromCase(caseId, trialId);
            logger.info("trial"+trialId+" deleted");
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting trial from case: " + e.getMessage());
        }
    }
    @GetMapping("/{caseId}/getTrials")
    public ResponseEntity<List<Trial>> getTrialsByCaseId(@PathVariable Long caseId) {
        List<Trial> trials = caseService.getTrialsByCaseId(caseId);
        return ResponseEntity.ok().body(trials);
    }
*/



    // CaseController.java
    @PostMapping("/{caseId}/addContributor")
    public ResponseEntity<?> addContributorToCase(@PathVariable Long caseId, @RequestBody ContributorDto contributorDto) {
        try {
            // Call the service method passing the case ID and contributor DTO
            caseService.addContributorToCase(caseId, contributorDto);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding contributor to case: " + e.getMessage());
        }
    }
    @PutMapping("/contributor/{contributorId}")
    public ResponseEntity<?> updateContributor(@PathVariable Long contributorId, @RequestBody ContributorDto contributorDto) {
        try {
            caseService.updateContributor(contributorId, contributorDto);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating contributor: " + e.getMessage());
        }
    }


    @GetMapping("/{caseId}/contributors")
    public ResponseEntity<List<Contributor>> getContributorsByCaseId(@PathVariable Long caseId) {
        try {
            List<Contributor> contributors = caseService.getContributorsByCaseId(caseId);
            return ResponseEntity.ok(contributors);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{caseId}/deleteContributor/{contributorId}")
    public ResponseEntity<?> deleteContributor(@PathVariable Long caseId, @PathVariable Long contributorId) {
        try {
            caseService.deleteContributorFromCase(caseId, contributorId);
            logger.info("contributor"+contributorId+" deleted");
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting contributor from case: " + e.getMessage());
        }
    }
    @GetMapping("/contributor/{idContributor}")
    public ResponseEntity<Contributor> getContributorById(@PathVariable Long idContributor) {
        Contributor trial = caseService.getContributorById(idContributor);
        if (trial != null) {
            return ResponseEntity.ok(trial);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    }


