package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.*;
import com.example.gestion_user.entities.enums.FolderStatus;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FolderDto;
import com.example.gestion_user.repositories.CaseRepository;
import com.example.gestion_user.repositories.FolderRepository;
import com.example.gestion_user.services.AlfrescoService;
import com.example.gestion_user.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private AlfrescoService alfrescoService;
    public Folder addFolder(FolderDto f, Long caseId) {
        // Fetch the case from the database
        Optional<Case> optionalCase = caseRepository.findById(caseId);
        if (optionalCase.isPresent()) {
            Case caseInstance = optionalCase.get();
            // Check if the case already has a folder associated with it
            if (caseInstance.getFolder() != null) {
                throw new NotFoundException("A folder already exists for the case with ID " + caseId);
            }
            // Create a new folder
            Folder folder = new Folder();
            folder.setName(f.getName());
            folder.setStatus(FolderStatus.NON_ARCHIVE);
            folder.setCreationDate(new Date());
            folder.setCaseInstance(caseInstance); // Set the case instance for the folder

            try {
                // Save the folder to the database
                folder = folderRepository.save(folder);
                // Call the Alfresco service to create the folder in Alfresco
                alfrescoService.createFolder(folder.getName()); // Assuming you want to use the folder name
                return folder;
            } catch (Exception ex) {
                throw new NotFoundException("Failed to create folder: " + ex.getMessage());
            }
        } else {
            throw new NotFoundException("Case with ID " + caseId + " not found");
        }
    }
//    @Override
//    public Folder updateFolder(Long id, FolderDto updatedFolderDto) {
//        // Find the existing Folder entity by ID
//        Folder existingFolder = folderRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Folder not found with id: " + id));
//
//        // Update the fields of the existing Folder entity with values from the DTO
//        existingFolder.setName(updatedFolderDto.getName());
//        existingFolder.setStatus(updatedFolderDto.getStatus());
//
//        // Save the updated Folder entity
//        try {
//            // Update the folder name in Alfresco
//            alfrescoService.updateFolderName(existingFolder.getName(), updatedFolderDto.getName());
//
//            return folderRepository.save(existingFolder);
//        } catch (Exception ex) {
//            throw new NotFoundException("Failed to update folder with id: " + id + ". " + ex.getMessage());
//        }
//    }


    @Override
    public void deleteFolder(Long idFolder ) {
        Folder folder = folderRepository.findById(idFolder)
                .orElseThrow(() -> new NotFoundException("Folder not found"));

        folderRepository.deleteById(idFolder);
        alfrescoService.deleteFolder(folder.getName());

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

    @Override
    public Folder updateFolder(Long id, FolderDto updatedFolderDto) {
        // Find the existing Folder entity by ID
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Folder not found with id: " + id));
        // Update the fields of the existing Folder entity with values from the DTO
        folder.setName(updatedFolderDto.getName());
        folder.setStatus(updatedFolderDto.getStatus());
        // Save the updated Folder entity
        try {

            alfrescoService.updateFolderName(folder.getName(), updatedFolderDto.getName());
            return folderRepository.save(folder);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update folder with id: " + id + ". " + ex.getMessage());
        }
    }

}
