package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Court;
import com.example.gestion_user.services.CourtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Court")
public class CourtController {

    @Autowired
    CourtService courtService;

    @GetMapping("/getCourts")
    public List<Court> getCourts() {
        return courtService.getCourts();
    }

    @PostMapping("/addCourt")
    public Court addCourt(@RequestBody Court c){
        return courtService.addCourt(c);

    }
    @GetMapping("/getCourtById/{idCourt}")
    public Court getCourtById(@PathVariable Integer idCourt) {
        return courtService.getCourtById(idCourt);
    }

    @PutMapping("/updateCourt/{idCourt}")
    public ResponseEntity<Court> updateCourt(@PathVariable Integer idCourt, @RequestBody Court updatedCourt) {
        // Assuming your CourtService has a method for retrieving and updating a court
        Court existingCourt = courtService.getCourtById(idCourt);

        if (existingCourt == null) {
            // If the court doesn't exist, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }

        // Update fields that are not null in the updatedCourt
        if (updatedCourt.getGovernorate() != null) {
            existingCourt.setGovernorate(updatedCourt.getGovernorate());
        }
        if (updatedCourt.getMunicipality() != null) {
            existingCourt.setMunicipality(updatedCourt.getMunicipality());
        }
        if (updatedCourt.getAdress() != null) {
            existingCourt.setAdress(updatedCourt.getAdress());
        }
        if (updatedCourt.getPhone() != null) {
            existingCourt.setPhone(updatedCourt.getPhone());
        }
        if (updatedCourt.getType() != null) {
            existingCourt.setType(updatedCourt.getType());
        }
        // Update other fields similarly (e.g., trials list)

        // Save the updated court
        Court updatedEntity = courtService.updateCourt(existingCourt);

        // Return the updated court with a 200 OK status
        return ResponseEntity.ok(updatedEntity);
    }


    @DeleteMapping("/deleteCourt/{idCourt}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Integer idCourt) {
        courtService.deleteCourt(idCourt);
        return ResponseEntity.noContent().build();
    }
}
