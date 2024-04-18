package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.*;
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
    public List<Folder> getFolders() {
        return folderRepository.findAll();
    }




    @Override
    public Folder getFolderById(Integer idFolder) {
        return( folderRepository.findById(idFolder).orElse(null));
    }

    @Override
    public Folder addFolder(Folder f) {
        return folderRepository.save(f) ;
    }
    @Override
    public Folder updateFolder(Folder c) {
        return folderRepository.save(c);
    }

    @Override
    public void deleteFolder(Integer idFolder) {
        folderRepository.deleteById(idFolder);
    }

    public Folder getFolderByName(String name) {
        return folderRepository.getFolderByName(name);
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
