package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Court;
import com.example.gestion_user.models.request.CourtDto;
import com.example.gestion_user.services.CourtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Court")
public class CourtController {

    @Autowired
    CourtService courtService;



    @PostMapping
    public Court addCourt(@RequestBody CourtDto c){
        return courtService.addCourt(c);

    }

    @PutMapping("/{idCourt}")
    public ResponseEntity<Court> updateCourt(@PathVariable Long idCourt, @RequestBody CourtDto updatedCourt) {
        // Assuming your CourtService has a method for retrieving and updating a court
        Court existingCourt = courtService.getCourtById(idCourt);

        if (existingCourt == null) {
            // If the court doesn't exist, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }

        // Save the updated court
        Court updatedEntity = courtService.updateCourt(idCourt,updatedCourt);

        // Return the updated court with a 200 OK status
        return ResponseEntity.ok(updatedEntity);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idCourt}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long idCourt) {
        courtService.deleteCourt(idCourt);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public List<Court> getCourts() {
        return courtService.getCourts();
    }

    @GetMapping("/{idCourt}")
    public Court getCourtById(@PathVariable Long idCourt) {
        return courtService.getCourtById(idCourt);
    }
}
