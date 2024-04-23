package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.*;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FolderDto;
import com.example.gestion_user.repositories.FolderRepository;
import com.example.gestion_user.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Override
    public Folder addFolder(FolderDto f) {
        Folder folder=new Folder();
        folder.setName(f.getName());
        folder.setStatus(f.getStatus());
        try {
            return folderRepository.save(folder) ;
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create court: " + ex.getMessage());
        }
    }
    @Override
    public Folder updateFolder(Long id, FolderDto updatedFolderDto) {
        // Find the existing Folder entity by ID
        Folder existingFolder = folderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Folder not found with id: " + id));

        // Update the fields of the existing Folder entity with values from the DTO
        existingFolder.setName(updatedFolderDto.getName());
        existingFolder.setStatus(updatedFolderDto.getStatus());

        // Save the updated Folder entity
        try {
            return folderRepository.save(existingFolder);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update folder with id: " + id + ". " + ex.getMessage());
        }
    }

    @Override
    public void deleteFolder(Long idFolder) {
        folderRepository.deleteById(idFolder);
    }
    @Override
    public List<Folder> getFolders() {
        return folderRepository.findAll();
    }

    @Override
    public Folder getFolderById(Long idFolder) {
        return( folderRepository.findById(idFolder).orElse(null));
    }



    public Folder getFolderByName(String name) {
        return folderRepository.findByName(name);
    }

       /* @Override
    public Dossier ajouterDossier(Dossier dossier, Integer idPhase, List<Fichier> files) {
        // Assurez-vous que l'ID de phase est valide
        Phase phase = phaseRepository.findById(idPhase)
                .orElseThrow(() -> new IllegalArgumentException("Phase non trouvée avec l'ID: " + idPhase));

        // Associez le dossier à la phase
        dossier.setPhase(phase);

        // Ajoutez les fichiers au dossier
        dossier.setFiles(files);

        // Enregistrez le dossier dans la base de données
        return dossierRepository.save(dossier);
    }


    @Override
    public List<String> getNomsDossiers() {
        return folderRepository.findAllNomDossiers();
    }*/


}
