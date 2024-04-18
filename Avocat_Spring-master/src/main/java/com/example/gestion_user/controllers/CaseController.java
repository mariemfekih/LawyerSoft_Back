package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Contributor;  // Added import statement for Contributor
import com.example.gestion_user.entities.Trial;
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

    @GetMapping("/getCases")
    public ResponseEntity<List<Case>> getCases() {
        List<Case> listCases = caseService.getCases();
        return ResponseEntity.ok().body(listCases);
    }

    @GetMapping("/getCaseById/{idCase}")
    public ResponseEntity<Case> getCaseById(@PathVariable("idCase") Integer idCase) {
        Case caseById = caseService.getCaseById(idCase);
        if (caseById != null) {
            return ResponseEntity.ok().body(caseById);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addCase")
    public ResponseEntity<Case> addCase(@RequestBody Case c) {
        Case addedCase = caseService.addCase(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCase);
    }

    /*
     * Update Case
     */
    @PutMapping("/updateCase/{idCase}")
    public ResponseEntity<Case> updateCase(@PathVariable Integer idCase, @RequestBody Case updatedCase) {
        // Assuming your CaseService has a method for retrieving and updating a case
        Case existingCase = caseService.getCaseById(idCase);

        if (existingCase == null) {
            // If the case doesn't exist, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }

        // Update fields that are not null in the updatedCase
        if (updatedCase.getTitle() != null) {
            existingCase.setTitle(updatedCase.getTitle());
        }
        if (updatedCase.getDescription() != null) {
            existingCase.setDescription(updatedCase.getDescription());
        }
        if (updatedCase.getType() != null) {
            existingCase.setType(updatedCase.getType());
        }
        if (updatedCase.getCreationDate() != null) {
            existingCase.setCreationDate(updatedCase.getCreationDate());
        }
        if (updatedCase.getClosingDate() != null) {
            existingCase.setClosingDate(updatedCase.getClosingDate());
        }
        // Update other fields similarly

        // Save the updated case
        Case updatedEntity = caseService.updateCase(existingCase);

        // Return the updated case with a 200 OK status
        return ResponseEntity.ok(updatedEntity);
    }


    @DeleteMapping("/deleteCase/{idCase}")
    public ResponseEntity<Void> deleteCase(@PathVariable("idCase") Integer idCase) {
        caseService.deleteCase(idCase);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getCaseByTitle/{title}")
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
    public ResponseEntity<?> addTrialToCase(@PathVariable Integer case_id, @RequestBody Trial trial) {
        try {
            caseService.addTrialToCase(case_id, trial);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding trial to case: " + e.getMessage());
        }
    }

    @GetMapping("/{caseId}/getTrials")
    public ResponseEntity<List<Trial>> getTrialsByCaseId(@PathVariable Integer caseId) {
        List<Trial> trials = caseService.getTrialsByCaseId(caseId);
        return ResponseEntity.ok().body(trials);
    }

    @DeleteMapping("/{caseId}/deleteTrial/{trialId}")
    public ResponseEntity<Void> deleteTrialFromCase(@PathVariable Integer caseId, @PathVariable Integer trialId) {
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

    @PutMapping("/{caseId}/updateTrial/{trialId}")
    public ResponseEntity<Trial> updateTrial(@PathVariable Integer caseId, @PathVariable Integer trialId, @RequestBody Trial updatedTrial) {
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

    @PostMapping("/{caseId}/addContributor")
    public ResponseEntity<?> addContributorToCase(@PathVariable Integer caseId, @RequestBody Contributor contributor) {
        try {
            Case updatedCase = caseService.addContributorToCase(caseId, contributor);
            return ResponseEntity.ok(updatedCase); // Return the updated case with the added contributor
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding contributor to case: " + e.getMessage());
        }
    }
}
