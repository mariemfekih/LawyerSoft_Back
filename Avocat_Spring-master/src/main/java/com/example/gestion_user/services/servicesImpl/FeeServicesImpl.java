package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Fee;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FeeDto;
import com.example.gestion_user.repositories.FeeRepository;
import com.example.gestion_user.services.FeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FeeServicesImpl implements FeeService {

    @Autowired
    FeeRepository feeRepository ;

   /* @Autowired
    AffaireRepository affaireRepository ;*/

    @Override
    public Fee addFee(FeeDto f) {
        Fee fee = new Fee();
        fee.setAmount(f.getAmount());
        fee.setReference(f.getReference());
        fee.setDate(f.getDate());
        fee.setRemain(f.getRemain());
        fee.setType(f.getType());
        try {
            return feeRepository.save(fee);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create court: " + ex.getMessage());
        }
    }
    @Override
    public Fee updateFee(Long id, FeeDto updatedFeeDto) {
        // Find the existing Fee entity by ID
        Fee existingFee = feeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fee not found with id: " + id));

        // Update the fields of the existing Fee entity with values from the DTO
        existingFee.setAmount(updatedFeeDto.getAmount());
        existingFee.setReference(updatedFeeDto.getReference());
        existingFee.setDate(updatedFeeDto.getDate());
        existingFee.setRemain(updatedFeeDto.getRemain());
        existingFee.setType(updatedFeeDto.getType());

        // Save the updated Fee entity
        try {
            return feeRepository.save(existingFee);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update fee with id: " + id + ". " + ex.getMessage());
        }
    }


    @Override
    public void deleteFee(Long idFee) {
        feeRepository.deleteById(idFee);
    }
    @Override
    public List<Fee> getFees() {
        return feeRepository.findAll();
    }

    @Override
    public Fee getFeeById(Long idFee) {
        return feeRepository.findById(idFee).get() ;
    }

   /* @Override
    public void addHonoraireAndAffectToAffaire(Fee honoraire, Integer idAffaire) {
        Affaire affaire = affaireRepository.findById(idAffaire).orElse(null);
        honoraire.setAffaire(affaire);
        honoraireRepository.save(honoraire);

    }

  public byte[] generateQRCodeForHonoraire(Integer idHonoraire) throws Exception {
        Fee honoraire = honoraireRepository.findById(idHonoraire).orElse(null);
        if (honoraire == null) {
            throw new Exception("Honoraire not found");
        }

        // Création du contenu du QR code
        String qrCodeContent =
                "\nHonoraire : "
                        + "\nAmount: " + honoraire.getAmount()
                        + "\nDate: " + honoraire.getDate()
                        + "\nRef: " + honoraire.getRef()
                        + "\nType: " + honoraire.getType()
                        + "\nRemain: " + honoraire.getRemain();

        // Génération du QR code
        //Ecrire les données d'image générées dans un tableau de bytes.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //Encodage des données
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, 250, 250);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", baos);
        //convertire le contenu de l'objet en tableau de byes
        return baos.toByteArray();
    }*/
}
