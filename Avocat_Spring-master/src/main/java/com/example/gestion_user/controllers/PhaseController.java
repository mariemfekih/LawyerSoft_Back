package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Phase;
import com.example.gestion_user.services.PhaseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Phase")
public class PhaseController {
    @Autowired
    PhaseService phaseService;

    @GetMapping("/getPhases")
    public ResponseEntity<List<Phase>> getAllPhases() {
        List<Phase> phases = phaseService.getPhases();
        return ResponseEntity.ok(phases);
    }

    @GetMapping("/getPhaseById/{idPhase}")
    public ResponseEntity<Phase> getPhaseById(@PathVariable Integer idPhase) {
        Phase phase = phaseService.getPhaseById(idPhase);
        if (phase != null) {
            return ResponseEntity.ok(phase);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/addPhase")
    public ResponseEntity<Phase> addPhase(@RequestBody Phase phase) {
        Phase addedPhase = phaseService.addPhase(phase);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedPhase);
    }

    @PutMapping("/updatePhase")
    public ResponseEntity<Phase> updatePhase(@RequestBody Phase phase) {
        Phase updatedPhase = phaseService.updatePhase(phase);
        return ResponseEntity.ok(updatedPhase);
    }

    @DeleteMapping("/deletePhase/{idPhase}")
    public ResponseEntity<Void> deletePhase(@PathVariable Integer idPhase) {
        phaseService.deletePhase(idPhase);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getPhaseByTitle/{title}")
    public ResponseEntity<Phase> getPhaseByTitle(@PathVariable String title) {
        Phase phases = phaseService.getPhaseByTitle(title);
        return ResponseEntity.ok(phases);
    }
}
