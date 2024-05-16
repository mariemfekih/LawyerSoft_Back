package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.services.TrialService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Trial")
public class TrialController {
    @Autowired
    TrialService trialService;
    private static final Logger logger = LoggerFactory.getLogger(CaseController.class);


    @PostMapping("/{case_id}/addTrial/{court_id}")
    public ResponseEntity<?> addTrial(@PathVariable Long case_id,@PathVariable Long court_id, @RequestBody Trial trial) {
        try {
            trialService.addTrial(case_id,court_id, trial);
            logger.info("trial added");
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding trial : " + e.getMessage());
        }
    }

    @PutMapping("/{trialId}/updateTrial/{caseId}/{courtId}")
    public ResponseEntity<Trial> updateTrial(@PathVariable Long caseId,@PathVariable Long courtId, @PathVariable Long trialId, @RequestBody Trial updatedTrial) {
        try {
            trialService.updateTrial(caseId,courtId, trialId, updatedTrial);
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
    @PutMapping("/updateTrial")
    public ResponseEntity<Trial> updateTrial(@RequestBody Trial trial) {
        Trial updatedTrial = trialService.updateTrial(trial);
        return ResponseEntity.ok(updatedTrial);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(" /{idTrial}")
    public ResponseEntity<Void> deleteTrial(@PathVariable Long idTrial) {
        trialService.deleteTrial(idTrial);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Trial>> getTrials() {
        List<Trial> trials = trialService.getTrials();
        return ResponseEntity.ok(trials);
    }

    @GetMapping("/{idTrial}")
    public ResponseEntity<Trial> getTrialById(@PathVariable Long idTrial) {
        Trial trial = trialService.getTrialById(idTrial);
        if (trial != null) {
            return ResponseEntity.ok(trial);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
