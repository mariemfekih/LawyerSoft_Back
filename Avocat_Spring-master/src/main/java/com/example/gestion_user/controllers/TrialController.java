package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Trial;
import com.example.gestion_user.services.TrialService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Trial")
public class TrialController {
    @Autowired
    TrialService trialService;

    @GetMapping("/getTrials")
    public ResponseEntity<List<Trial>> getTrials() {
        List<Trial> trials = trialService.getTrials();
        return ResponseEntity.ok(trials);
    }

    @GetMapping("/getTrialById/{idTrial}")
    public ResponseEntity<Trial> getTrialById(@PathVariable Integer idTrial) {
        Trial trial = trialService.getTrialById(idTrial);
        if (trial != null) {
            return ResponseEntity.ok(trial);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addTrial")
    public ResponseEntity<Trial> addTrial(@RequestBody Trial trial) {
        Trial addedTrial = trialService.addTrial(trial);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedTrial);
    }

    @PutMapping("/updateTrial")
    public ResponseEntity<Trial> updateTrial(@RequestBody Trial trial) {
        Trial updatedTrial = trialService.updateTrial(trial);
        return ResponseEntity.ok(updatedTrial);
    }

    @DeleteMapping("/deleteTrial/{idTrial}")
    public ResponseEntity<Void> deleteTrial(@PathVariable Integer idTrial) {
        trialService.deleteTrial(idTrial);
        return ResponseEntity.noContent().build();
    }

}
