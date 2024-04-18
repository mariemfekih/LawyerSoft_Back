package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Fee;
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

    @GetMapping("/getFees")
    public ResponseEntity<List<Fee>> getFees() {
        List<Fee> listFees = feeService.getFees();
        return ResponseEntity.ok().body(listFees);
    }

    @GetMapping("/getFeeById/{fee-id}")
    public ResponseEntity<Fee> getFeeById(@PathVariable("fee-id") Integer feeId){
        Fee f = feeService.getFeeById(feeId);
        if (f != null) {
            return ResponseEntity.ok().body(f);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addFee")
    public ResponseEntity<Fee> addFee(@RequestBody Fee a){
        Fee fee = feeService.addFee(a);
        return ResponseEntity.status(HttpStatus.CREATED).body(fee);
    }

    @PutMapping("/updateFee")
    public ResponseEntity<Fee> updateFee(@RequestBody Fee a){
        Fee fee = feeService.updateFee(a);
        return ResponseEntity.ok().body(fee);
    }

    @DeleteMapping("/removeFee/{fee-id}")
    public ResponseEntity<Void> removeHonoraire(@PathVariable("fee-id") Integer feeId) {
        feeService.deleteFee(feeId);
        return ResponseEntity.noContent().build();
    }

    /*@PostMapping("/addHonnoraireAndAffectToAffaire/{affaire-id}")
    public ResponseEntity<Void> addHonnoraireAndAffectToAffaire(@RequestBody Fee honoraire, @PathVariable("affaire-id") Integer idAffaire ){
        honoraireService.addHonoraireAndAffectToAffaire(honoraire, idAffaire);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/generateQRCodeForHonoraire/{id-honoraire}")
    public ResponseEntity<byte[]> generateQRCodeForHonoraire(@PathVariable("id-honoraire") Integer idHonoraire) {
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
