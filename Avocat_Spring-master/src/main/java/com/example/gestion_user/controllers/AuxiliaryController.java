package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.services.AuxiliaryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Auxiliary")
public class AuxiliaryController {
    @Autowired
    AuxiliaryService auxiliaryService;

    @GetMapping("/getAuxiliaries")
    public ResponseEntity<List<Auxiliary>> getAuxiliaries() {
        List<Auxiliary> auxiliaries = auxiliaryService.getAuxiliaries();
        return ResponseEntity.ok(auxiliaries);
    }

    @GetMapping("/getAuxiliaryById/{idAuxiliary}")
    public ResponseEntity<Auxiliary> getAuxiliaryById(@PathVariable Integer idAuxiliary) {
        Auxiliary a = auxiliaryService.getAuxiliaryById(idAuxiliary);
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getAuxiliaryByCin/{cin}")
    public ResponseEntity<Auxiliary> getAuxiliaryByCin(@PathVariable Long cin) {
        Auxiliary a = auxiliaryService.getAuxiliaryByCin(cin);
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getAuxiliaryByEmail/{email}")
    public ResponseEntity<Auxiliary> getAuxiliaryByEmail(@PathVariable String email) {
        Auxiliary a = auxiliaryService.getAuxiliaryByEmail(email);
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addAuxiliary")
    public ResponseEntity<Auxiliary> addAuxiliary(@RequestBody Auxiliary a) {
        Auxiliary addedAuxiliary = auxiliaryService.addAuxiliary(a);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAuxiliary);
    }

    @PutMapping("/updateAuxiliary/{idAuxiliary}")
    public ResponseEntity<Auxiliary> updateAuxiliary(@PathVariable Integer idAuxiliary, @RequestBody Auxiliary updatedAuxiliary) {
        // Assuming your AuxiliaryService has a method for retrieving and updating an auxiliary
        Auxiliary existingAuxiliary = auxiliaryService.getAuxiliaryById(idAuxiliary);

        if (existingAuxiliary == null) {
            // If the auxiliary doesn't exist, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }

        // Update fields that are not null in the updatedAuxiliary
        if (updatedAuxiliary.getFirstName() != null) {
            existingAuxiliary.setFirstName(updatedAuxiliary.getFirstName());
        }
        if (updatedAuxiliary.getLastName() != null) {
            existingAuxiliary.setLastName(updatedAuxiliary.getLastName());
        }
        if (updatedAuxiliary.getCin() != null) {
            existingAuxiliary.setCin(updatedAuxiliary.getCin());
        }
        if (updatedAuxiliary.getEmail() != null) {
            existingAuxiliary.setEmail(updatedAuxiliary.getEmail());
        }
        if (updatedAuxiliary.getCity() != null) {
            existingAuxiliary.setCity(updatedAuxiliary.getCity());
        }
        if (updatedAuxiliary.getPhone() != null) {
            existingAuxiliary.setPhone(updatedAuxiliary.getPhone());
        }
        if (updatedAuxiliary.getBirthDate() != null) {
            existingAuxiliary.setBirthDate(updatedAuxiliary.getBirthDate());
        }
        if (updatedAuxiliary.getJob() != null) {
            existingAuxiliary.setJob(updatedAuxiliary.getJob());
        }

        // Save the updated auxiliary
        Auxiliary updatedEntity = auxiliaryService.updateAuxiliary(existingAuxiliary);

        // Return the updated auxiliary with a 200 OK status
        return ResponseEntity.ok(updatedEntity);
    }


    @DeleteMapping("/deleteAuxiliary/{idAuxiliary}")
    public ResponseEntity<Void> deleteAuxiliary(@PathVariable Integer idAuxiliary) {
        auxiliaryService.deleteAuxiliary(idAuxiliary);
        return ResponseEntity.noContent().build();
    }
}
