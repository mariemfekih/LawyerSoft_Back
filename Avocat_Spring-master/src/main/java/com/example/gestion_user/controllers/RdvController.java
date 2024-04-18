package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.services.RdvService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Rdv")
public class RdvController {
    @Autowired
    RdvService rdvService;

    @GetMapping("/getAllRdvs")
    public ResponseEntity<List<Appointment>> getAllRdvs() {
        List<Appointment> rdvs = rdvService.getRdvs();
        return ResponseEntity.ok(rdvs);
    }

    @GetMapping("/getRdvById/{idRdv}")
    public ResponseEntity<Appointment> getRdvById(@PathVariable Integer idRdv) {
        Appointment rdv = rdvService.getRdvById(idRdv);
        if (rdv != null) {
            return ResponseEntity.ok(rdv);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addRdv")
    public ResponseEntity<Appointment> addRdv(@RequestBody Appointment rdv) {
        Appointment addedRdv = rdvService.addRdv(rdv);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRdv);
    }

    @PutMapping("/updateRdv")
    public ResponseEntity<Appointment> updateRdv(@RequestBody Appointment rdv) {
        Appointment updatedRdv = rdvService.updateRdv(rdv);
        return ResponseEntity.ok(updatedRdv);
    }

    @DeleteMapping("/deleteRdv/{idRdv}")
    public ResponseEntity<Void> deleteRdv(@PathVariable Integer idRdv) {
        rdvService.deleteRdv(idRdv);
        return ResponseEntity.noContent().build();
    }
}
