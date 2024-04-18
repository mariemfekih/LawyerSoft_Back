package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Fee;
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
    public Fee addFee(Fee fee) {
        return feeRepository.save(fee);
    }

    @Override
    public List<Fee> getFees() {
        return feeRepository.findAll();
    }

    @Override
    public Fee updateFee(Fee fee) {
        return feeRepository.save(fee);
    }

    @Override
    public void deleteFee(Integer idFee) {
        feeRepository.deleteById(idFee);

    }

    @Override
    public Fee getFeeById(Integer idFee) {
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
