package com.example.gestion_user.services.servicesImpl;

//import com.example.gestion_user.entities.Action;
import com.example.gestion_user.entities.Action;
import com.example.gestion_user.entities.Customer;
import com.example.gestion_user.entities.Fee;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FeeDto;
import com.example.gestion_user.repositories.ActionRepository;
import com.example.gestion_user.repositories.CustomerRepository;
import com.example.gestion_user.repositories.FeeRepository;
import com.example.gestion_user.repositories.UserRepository;
import com.example.gestion_user.services.ActionService;
import com.example.gestion_user.services.FeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.List;

@AllArgsConstructor
@Service
public class FeeServicesImpl implements FeeService {

    @Autowired
    FeeRepository feeRepository ;

    @Autowired
    ActionService actionRepository ;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    UserRepository userRepository;

   /* @Autowired
    AffaireRepository affaireRepository ;*/

    @Override
    @Transactional
    public Fee addFee(FeeDto f, Long userId, Long customerId, List<Long> actionIds) {
        // Retrieve actions by IDs
        List<Action> actions = actionRepository.getActionsByIds(actionIds);

        // Calculate total amount from actions
        float totalAmount = 0;
        for (Action action : actions) {
            totalAmount += action.getAmount();
        }

        // Retrieve User and Customer entities
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        // Create Fee entity
        Fee fee = new Fee();
        fee.setAmount(f.getAmount());
        fee.setReference(f.getReference());
        fee.setDate(f.getDate());
        fee.setRemain(f.getRemain());
        fee.setUserInstance(user); // Set user relationship
        fee.setCustomer(customer); // Set customer relationship
        fee.setAmount(totalAmount); // Set total amount

        try {
            return feeRepository.save(fee);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to create fee: " + ex.getMessage());
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
       existingFee.setRemain(existingFee.getRemain());
       // existingFee.setType(updatedFeeDto.getType());

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

    @Override
    public Fee AddAndAssignFeeToAction(Fee fee, List<Long> actionIds)
    {
        List<Action> actions = actionRepository.getActionsByIds(actionIds);

        float totalAmount = 0;
        for (Action action : actions) {
            totalAmount += action.getAmount();
        }

        fee.setAmount(totalAmount);

        feeRepository.save(fee);

        return fee;
    }


   /* @Override
    public void addHonoraireAndAffectToAffaire(Fee honoraire, Long idAffaire) {
        Affaire affaire = affaireRepository.findById(idAffaire).orElse(null);
        honoraire.setAffaire(affaire);
        honoraireRepository.save(honoraire);

    }

  public byte[] generateQRCodeForHonoraire(Long idHonoraire) throws Exception {
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
