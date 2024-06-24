package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.controllers.CaseController;
import com.example.gestion_user.entities.*;
import com.example.gestion_user.entities.enums.FolderStatus;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FolderDto;
import com.example.gestion_user.repositories.*;
import com.example.gestion_user.services.AlfrescoService;
import com.example.gestion_user.services.FolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FileRepository fileRepository;
@Autowired
    UserRepository userRepository;
@Autowired
CaseRepository caseRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AlfrescoService alfrescoService;
    private static final Logger log = LoggerFactory.getLogger(CaseController.class);

    public String addFolder( Long cutomerId) {
        // Fetch the case from the database
        Optional<Customer> optionalCustomer = customerRepository.findById(cutomerId);
        if (optionalCustomer.isPresent()) {
            Customer customerInstance = optionalCustomer.get();
            // Check if the customer already has a folder associated with it
            if (customerInstance.getFolder()!= null) {
                throw new NotFoundException("A folder already exists for the customer with ID " + cutomerId);
            }
            // Create a new folder
            Folder folder = new Folder();
            folder.setName(customerInstance.getFirstName() + customerInstance.getLastName());
         //   folder.setStatus(FolderStatus.NON_ARCHIVE);
            folder.setCreationDate(new Date());
            folder.setCustomerInstance(customerInstance); // Set the case instance for the folder

            try {
                // Call the Alfresco service to create the folder in Alfresco
                String folderId = alfrescoService.addFolder(folder.getName());
                // Save the folder to the database
                folder.setPath(folderId);
                folder = folderRepository.save(folder);
                return folderId; // Return the ID of the newly created folder
            } catch (Exception ex) {
                throw new NotFoundException("Failed to create folder: " + ex.getMessage());
            }
        } else {
            throw new NotFoundException("Customer with ID " + cutomerId + " not found");
        }
    }
    @Override
    @Transactional
    public void deleteFolderByPath(String path) {
        System.out.println("Attempting to find folder with path: " + path);

        // Fetch the folder ID by its path
        Long folderIdFromPath = getIdByPath(path);

        // Check if folderIdFromPath is null, indicating the folder doesn't exist with the provided path
        if (folderIdFromPath != null) {
            // Delete the folder from Alfresco using the folder's path (or ID in Alfresco)
            System.out.println("Deleting folder from Alfresco with path or ID: " + path);
            alfrescoService.deleteFolderById(path);

            // Delete the folder entity from the database using its ID
            System.out.println("Deleting folder entity from database with ID: " + folderIdFromPath);
            try {
                folderRepository.deleteById(folderIdFromPath);
            } catch (Exception e) {
                System.err.println("Error deleting folder entity: " + e.getMessage());
                e.printStackTrace(); // Add stack trace to understand the root cause
                throw e; // Rethrow the exception to propagate the error
            }
        } else {
            throw new NotFoundException("Folder not found with path: " + path);
        }
    }


//    @Override
//    @Transactional
//    public void deleteFolderByPath(String path) {
//        System.out.println("Attempting to find folder with path: " + path);
//
//        // Fetch the folder ID by its path
//        Long folderIdFromPath = getIdByPath(path);
//
//        // Check if folderIdFromPath is null, indicating the folder doesn't exist with the provided path
//        if (folderIdFromPath != null) {
//            // Delete the folder from Alfresco using the folder's path (or ID in Alfresco)
//            System.out.println("Deleting folder from Alfresco with path or ID: " + path);
//            alfrescoService.deleteFolderById(path);
//
//            // Delete the folder entity from the database using its ID
//            System.out.println("Deleting folder entity from database with ID: " + folderIdFromPath);
//            try {
//                Folder folder = folderRepository.findById(folderIdFromPath).get();
//                deleteFolderRecursively(folder);
//                folderRepository.deleteById(folderIdFromPath);
//            } catch (Exception e) {
//                System.err.println("Error deleting folder entity: " + e.getMessage());
//                e.printStackTrace(); // Add stack trace to understand the root cause
//                throw e; // Rethrow the exception to propagate the error
//            }
//        } else {
//            throw new NotFoundException("Folder not found with path: " + path);
//        }
//    }
//
//    private void deleteFolderRecursively(Folder folder) {
//        // Delete all files in the folder
//        List<File> files = folder.getFiles();
//        for (File file : files) {
//            // Delete the file from Alfresco
//            alfrescoService.deleteFileById(file.getPath());
//            // Delete the file entity from the database
//            fileRepository.deleteById(file.getIdFile());
//        }
//
//        // Delete all subfolders in the folder
//        List<Folder> subfolders = folderRepository.findByParentFolder(folder);
//        for (Folder subfolder : subfolders) {
//            deleteFolderRecursively(subfolder);
//        }
//
//        // Delete the folder from Alfresco
//        alfrescoService.deleteFolderById(folder.getPath());
//        // Delete the folder entity from the database
//        folderRepository.deleteById(folder.getIdFolder());
//    }

    /*    @Override
    @Transactional
    public void deleteFolderByPath(String path) {
        System.out.println("Attempting to find folder with path: " + path);

        // Fetch the folder ID by its path
        Long folderIdFromPath = getIdByPath(path);

        // Check if folderIdFromPath is null, indicating the folder doesn't exist with the provided path
        if (folderIdFromPath != null) {
            // Delete the folder from Alfresco using the folder's path (or ID in Alfresco)
            System.out.println("Deleting folder from Alfresco with path or ID: " + path);
            alfrescoService.deleteFolderById(path);

            // Delete the folder entity from the database using its ID
            System.out.println("Deleting folder entity from database with ID: " + folderIdFromPath);
            try {
                folderRepository.deleteById(folderIdFromPath);
            } catch (Exception e) {
                System.err.println("Error deleting folder entity: " + e.getMessage());
                e.printStackTrace(); // Add stack trace to understand the root cause
                throw e; // Rethrow the exception to propagate the error
            }
        } else {
            throw new NotFoundException("Folder not found with path: " + path);
        }
    }*/
/*    @Override
    public void deleteFolder(Long idFolder ) {
        Folder folder = folderRepository.findById(idFolder)
                .orElseThrow(() -> new NotFoundException("Folder not found"));

        folderRepository.deleteById(idFolder);
        alfrescoService.deleteFolder(folder.getName());

    }*/
    @Override
    public List<Folder> getFolders() {
        try {
            return folderRepository.findAll();
        } catch (Exception e) {
            // Handle exceptions and log the error
            log.error("Error fetching folders from database", e);
            throw new RuntimeException("Failed to fetch folders");
        }
    }

    @Override
    public Folder getFolderById(Long idFolder) {
        return( folderRepository.findById(idFolder).orElse(null));
    }



    public Folder getFolderByName(String name) {
        return folderRepository.findByName(name);
    }

    @Override
    @Transactional
    public void updateFolderName(String path, String newFolderName) {
        System.out.println("Attempting to find folder with path: " + path);

        // Fetch the folder ID by its path
        Long folderIdFromPath = getIdByPath(path);

        // Check if folderIdFromPath is null, indicating the folder doesn't exist with the provided path
        if (folderIdFromPath!= null) {
            // Update the folder name in Alfresco using the folder's path (or ID in Alfresco)
            System.out.println("Updating folder in Alfresco with path: " + path);
            alfrescoService.updateFolderName(path, newFolderName);

            // Update the folder entity in the database using its ID
            System.out.println("Updating folder entity in database with ID: " + folderIdFromPath);
            try {
                Folder folder = folderRepository.findById(folderIdFromPath)
                        .orElseThrow(() -> new NotFoundException("Folder not found with ID: " + folderIdFromPath));
                folder.setName(newFolderName);
                folderRepository.save(folder);
            } catch (Exception e) {
                System.err.println("Error updating folder entity: " + e.getMessage());
                e.printStackTrace(); // Add stack trace to understand the root cause
                throw e; // Rethrow the exception to propagate the error
            }
        } else {
            throw new NotFoundException("Folder not found with path: " + path + ";1.0");
        }
    }
    @Override
    public Long getIdByPath(String path) {
        Optional<Folder> optionalFolder = folderRepository.findByPath(path);
        return optionalFolder.map(Folder::getIdFolder).orElse(null);
    }


    @Override
    public Folder addSubFolder(Long caseId, Long parentFolderId) {
        // Fetch the parent folder from the database
        Folder parentFolder = folderRepository.findById(parentFolderId)
                .orElseThrow(() -> new NotFoundException("Parent folder with ID " + parentFolderId + " not found"));

        // Fetch the case from the database
        Case caseObj = caseRepository.findById(caseId)
                .orElseThrow(() -> new NotFoundException("Case with ID " + caseId + " not found"));

        // Create a new sub-folder
        Folder subFolder = new Folder();
        subFolder.setName(caseObj.getTitle()); // Set the name of the sub-folder to the name of the case
      //  subFolder.setStatus(FolderStatus.NON_ARCHIVE);
        subFolder.setCreationDate(new Date());
        subFolder.setParentFolder(parentFolder); // Set the parent folder
        subFolder.setCustomerInstance(parentFolder.getCustomerInstance()); // Set the customer instance for the sub-folder
        subFolder.setCaseInstance(caseObj);

        try {


            // Assuming AlfrescoService method to create folder in Alfresco
            String folderId = alfrescoService.createSubFolder(parentFolder.getName(), subFolder.getName());
            subFolder.setPath(folderId);
            subFolder = folderRepository.save(subFolder);
            return subFolder;
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create sub-folder: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public List<Folder> getSubfolders(Long parentFolderId) {
        List<Folder> subfolders = folderRepository.findByParentFolderId(parentFolderId);
        return subfolders;
    }
@Override
    public List<Folder> getFoldersByUserId(Long userId) {
        List<Folder> folders = folderRepository.findFoldersByUserId(userId);
        return folders;
    }
    @Override
    public String addFolderByName(String folderName) {
        String alfrescoFolderId;
        if (alfrescoService.folderExists(folderName)) {
            alfrescoFolderId = alfrescoService.getFolderId(folderName);
        } else {
            alfrescoFolderId = alfrescoService.addFolder(folderName);
        }
        Folder folder = new Folder();
        folder.setName(folderName);
        folder.setCreationDate(new Date());

        try {
            // Call the Alfresco service to create the folder in Alfresco
            String folderId = alfrescoService.addFolder(folder.getName());
            // Save the folder to the database
            folder.setPath(folderId);
            folder = folderRepository.save(folder);
            return folderId; // Return the ID of the newly created folder
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create folder: " + ex.getMessage());
        }
    }

}
