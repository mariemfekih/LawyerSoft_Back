package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Court;
import com.example.gestion_user.entities.Fee;
import com.example.gestion_user.models.request.FeeDto;
import com.example.gestion_user.services.FeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Fee")
public class FeeController {

    @Autowired
    FeeService feeService ;



    @PostMapping("/{userId}/{customerId}")
    public ResponseEntity<Fee> addFee(@RequestBody FeeDto feeDto,
                                      @PathVariable Long userId,
                                      @PathVariable Long customerId,
                                      @RequestParam List<Long> actionIds) {
        try {
            Fee fee = feeService.addFee(feeDto, userId, customerId, actionIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(fee);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fee> updateFee(@PathVariable Long id,@RequestBody FeeDto updatedFee){
        Fee existingFee = feeService.getFeeById(id);
        if (existingFee == null) {
            return ResponseEntity.notFound().build();
        }
        Fee fee = feeService.updateFee(id,updatedFee);
        return ResponseEntity.ok().body(fee);
    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{fee-id}")
    public ResponseEntity<Void> deleteFee(@PathVariable("fee-id") Long feeId) {
        feeService.deleteFee(feeId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<Fee>> getFees() {
        List<Fee> listFees = feeService.getFees();
        return ResponseEntity.ok().body(listFees);
    }

    @GetMapping("/{fee-id}")
    public ResponseEntity<Fee> getFeeById(@PathVariable("fee-id") Long feeId){
        Fee f = feeService.getFeeById(feeId);
        if (f != null) {
            return ResponseEntity.ok().body(f);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/assign-fee-to-actions")
    public ResponseEntity<Fee> addAndAssignFeeToActions(@RequestBody Fee fee, @RequestParam List<Long> actionIds) {
        Fee assignedFee = feeService.AddAndAssignFeeToAction(fee, actionIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignedFee);
    }


    /*@PostMapping("/addHonnoraireAndAffectToAffaire/{affaire-id}")
    public ResponseEntity<Void> addHonnoraireAndAffectToAffaire(@RequestBody Fee honoraire, @PathVariable("affaire-id") Long idAffaire ){
        honoraireService.addHonoraireAndAffectToAffaire(honoraire, idAffaire);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/generateQRCodeForHonoraire/{id-honoraire}")
    public ResponseEntity<byte[]> generateQRCodeForHonoraire(@PathVariable("id-honoraire") Long idHonoraire) {
        try {
            byte[] qrCode = honoraireService.generateQRCodeForHonoraire(idHonoraire);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return ResponseEntity.ok().headers(headers).body(qrCode);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }*/
}
