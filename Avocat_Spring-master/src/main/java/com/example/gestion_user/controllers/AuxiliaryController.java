package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Auxiliary;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.report.Report;
import com.example.gestion_user.entities.report.StringResult;
import com.example.gestion_user.models.request.AuxiliaryDto;
import com.example.gestion_user.services.AuxiliaryService;
import com.example.gestion_user.services.JasperReportService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Auxiliary")
public class AuxiliaryController {
    @Autowired
    AuxiliaryService auxiliaryService;
    private JasperReportService jasperReportService;

    @PostMapping("/{userId}")
    public ResponseEntity<Auxiliary> addAuxiliary(@RequestBody AuxiliaryDto a, @PathVariable Long userId) {
        Auxiliary addedAuxiliary = auxiliaryService.addAuxiliary(a,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedAuxiliary);
    }

    @PutMapping("/{idAuxiliary}")
    public ResponseEntity<Auxiliary> updateAuxiliary(@PathVariable Long idAuxiliary, @RequestBody AuxiliaryDto updatedAuxiliary) {
        // Assuming your AuxiliaryService has a method for retrieving and updating an auxiliary
        Auxiliary existingAuxiliary = auxiliaryService.getAuxiliaryById(idAuxiliary);

        if (existingAuxiliary == null) {
            // If the auxiliary doesn't exist, return a 404 Not Found response
            return ResponseEntity.notFound().build();
        }
        // Save the updated auxiliary
        Auxiliary updatedEntity = auxiliaryService.updateAuxiliary(idAuxiliary,updatedAuxiliary);

        // Return the updated auxiliary with a 200 OK status
        return ResponseEntity.ok(updatedEntity);
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idAuxiliary}")
    public ResponseEntity<Void> deleteAuxiliary(@PathVariable Long idAuxiliary) {
        auxiliaryService.deleteAuxiliary(idAuxiliary);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<Auxiliary>> getAuxiliaries() {
        List<Auxiliary> auxiliaries = auxiliaryService.getAuxiliaries();
        return ResponseEntity.ok(auxiliaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auxiliary> getAuxiliaryById(@PathVariable Long id) {
        Auxiliary a = auxiliaryService.getAuxiliaryById(id);
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Auxiliary>> getUserAuxiliaries(@PathVariable Long userId) {
        List<Auxiliary> auxiliaries = auxiliaryService.getUserAuxiliary(userId);
        return ResponseEntity.ok(auxiliaries);
    }

    @GetMapping("/cin/{cin}")
    public ResponseEntity<Auxiliary> getAuxiliaryByCin(@PathVariable String cin) {
        Auxiliary a = auxiliaryService.getAuxiliaryByCin(cin);
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<Auxiliary> getAuxiliaryByEmail(@PathVariable String email) {
        Auxiliary a = auxiliaryService.getAuxiliaryByEmail(email);
        if (a != null) {
            return ResponseEntity.ok(a);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/printAuxiliary")
    public StringResult printAuxiliary(@RequestBody Report report) {
        StringResult reportName=null;
        try{
            reportName=this.jasperReportService.createReport(report);
        }catch(SQLException e){ e.printStackTrace();}
        return reportName;
    }
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Long> getTotalAuxiliariesByUser(@PathVariable Long userId) {
        Long totalCases = auxiliaryService.getTotalAuxiliariesByUser(userId);
        return new ResponseEntity<>(totalCases, HttpStatus.OK);
    }
}
