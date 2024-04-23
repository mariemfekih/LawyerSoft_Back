package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Contributor;  // Added import statement for Contributor
import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.models.request.CaseDto;
import com.example.gestion_user.services.CaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
@RestController
@RequestMapping("/Case")
@CrossOrigin("http://localhost:4200")

public class CaseController {

    @Autowired
    CaseService caseService;
    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);



    @PostMapping
    public ResponseEntity<Case> addCase(@RequestBody CaseDto c) {
        Case addedCase = caseService.addCase(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCase);
    }

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

    @GetMapping("/{idCase}")
    public ResponseEntity<Case> getCaseById(@PathVariable("idCase") Long idCase) {
        Case caseById = caseService.getCaseById(idCase);
        if (caseById != null) {
            return ResponseEntity.ok().body(caseById);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{title}")
    public ResponseEntity<Case> getCaseByTitle(@PathVariable("title") String title) {
        Case casetitle = caseService.getCaseByTitle(title);
        if (casetitle != null) {
            return ResponseEntity.ok().body(casetitle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    ///////////////Add trial to case:
    @PostMapping("/{case_id}/addTrial")
    public ResponseEntity<?> addTrialToCase(@PathVariable Long case_id, @RequestBody Trial trial) {
        try {
            caseService.addTrialToCase(case_id, trial);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding trial to case: " + e.getMessage());
        }
    }
    @PutMapping("/{caseId}/updateTrial/{trialId}")
    public ResponseEntity<Trial> updateTrial(@PathVariable Long caseId, @PathVariable Long trialId, @RequestBody Trial updatedTrial) {
        try {
            caseService.updateTrial(caseId, trialId, updatedTrial);
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
    public ResponseEntity<Void> deleteTrialFromCase(@PathVariable Long caseId, @PathVariable Long trialId) {
        try {
            caseService.deleteTrialFromCase(caseId, trialId);
            logger.info("khedmeeet");
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("An error occurred while deleting trial from case", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{caseId}/getTrials")
    public ResponseEntity<List<Trial>> getTrialsByCaseId(@PathVariable Long caseId) {
        List<Trial> trials = caseService.getTrialsByCaseId(caseId);
        return ResponseEntity.ok().body(trials);
    }




   /* @PostMapping("/{caseId}/addContributor")
    public ResponseEntity<?> addContributoreToCas(@PathVariable Integer caseId, @RequestBody Contributor contributor) {
        try {
            Case updatedCase = caseService.addContributorToCase(caseId, contributor);
            return ResponseEntity.ok(updatedCase); // Return the updated case with the added contributor
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding contributor to case: " + e.getMessage());
        }
    }*/

    // CaseController.java
    @PostMapping("/{caseId}/addContributor")
    public ResponseEntity<Case> addContributorToCase(@PathVariable Long caseId, @RequestBody Contributor contributor) {
        try {
            Case updatedCase = caseService.addContributorToCase(caseId, contributor);
            return ResponseEntity.ok(updatedCase); // Return the updated case with the added contributor
        } catch (EntityNotFoundException e) {
            // Return 404 Not Found if the case is not found
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log the error for debugging purposes
            logger.error("Error adding contributor to case: {}", e.getMessage(), e);

            // Return 500 Internal Server Error with an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
