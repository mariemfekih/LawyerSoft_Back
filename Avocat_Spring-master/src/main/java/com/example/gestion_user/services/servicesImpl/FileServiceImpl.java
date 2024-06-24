package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.entities.enums.FolderStatus;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FileDto;
import com.example.gestion_user.repositories.FileRepository;
import com.example.gestion_user.repositories.FolderRepository;
import com.example.gestion_user.services.AlfrescoService;
import com.example.gestion_user.services.FileService;
import com.example.gestion_user.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileRepository filerepository;
    @Autowired
    FolderRepository folderrepository;
    @Autowired
    private AlfrescoService alfrescoService;
    @Autowired
    private FolderService folderService;
    @Override
    @Transactional
    public String addFileToFolder(Long folderId, MultipartFile file) throws IOException {
        // Get the folder by ID from your repository
        Folder folder = folderrepository.findById(folderId)
                .orElseThrow(() -> new NotFoundException("Folder not found: " + folderId));

        // Prepare properties for the file
        String fileName = file.getOriginalFilename();
        String fileId;

        try {
            // Call AlfrescoService to add the file to the folder in Alfresco
            fileId = alfrescoService.addFileToFolder(folder.getName(), file); // Assuming Alfresco service still needs folder name
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Alfresco: " + e.getMessage());
        }

        // Save file metadata to database
        File fileEntity = new File();
        fileEntity.setName(fileName);
        fileEntity.setPath(fileId); // Assuming fileId from Alfresco can be used as path
        fileEntity.setCreationDate(new Date());
        fileEntity.setFolder(folder);
        fileEntity = filerepository.save(fileEntity);

        return fileEntity.getIdFile().toString(); // Return the ID of the uploaded file
    }

    @Override
    @Transactional
    public void updateFileName(String path, String newName) {
        System.out.println("Attempting to find file with path: " + path);

        // Fetch the file ID by its path
        Long fileIdFromPath = getIdByPath(path);

        // Check if fileIdFromPath is null, indicating the file doesn't exist with the provided path
        if (fileIdFromPath != null) {
            // Update the file name in Alfresco using the file's path (or ID in Alfresco)
            System.out.println("Updating file in Alfresco with path: " + path);
            alfrescoService.updateFileName(path, newName);

            // Update the file entity in the database using its ID
            System.out.println("Updating file entity in database with ID: " + fileIdFromPath);
            try {
                File file = filerepository.findById(fileIdFromPath)
                        .orElseThrow(() -> new NotFoundException("File not found with ID: " + fileIdFromPath));
                file.setName(newName);
                filerepository.save(file);
            } catch (Exception e) {
                System.err.println("Error updating file entity: " + e.getMessage());
                e.printStackTrace(); // Add stack trace to understand the root cause
                throw e; // Rethrow the exception to propagate the error
            }
        } else {
            throw new NotFoundException("File not found with path: " + path + ";1.0");
        }
    }

    @Override
    @Transactional
    public void deleteFileByPath(String path) {
        System.out.println("Attempting to find file with ID: " + path);

        // Fetch the file ID by its path
        Long fileIdFromPath = getIdByPath(path);

        // Check if fileIdFromPath is null, indicating the file doesn't exist with the provided path
        if (fileIdFromPath != null) {
            // Delete the file from Alfresco using the file's path (or ID in Alfresco)
            System.out.println("Deleting file from Alfresco with path or ID: " + path);
            alfrescoService.deleteFileById(path);

            // Delete the file entity from the database using its ID
            System.out.println("Deleting file entity from database with ID: " + fileIdFromPath);
            try {
                filerepository.deleteById(fileIdFromPath);
            } catch (Exception e) {
                System.err.println("Error deleting file entity: " + e.getMessage());
                e.printStackTrace(); // Add stack trace to understand the root cause
                throw e; // Rethrow the exception to propagate the error
            }
        } else {
            throw new NotFoundException("File not found with path: " + path + ";1.0");
        }
    }



    @Override
    public Long getIdByPath(String path) {
        Optional<File> optionalFile = filerepository.findByPath(path + ";1.0");
        return optionalFile.map(File::getIdFile).orElse(null);
    }



    @Override
    public List<File> getFiles() {
        return filerepository.findAll();
    }



    @Override
    public File getFileById(Long idFile) {
        return filerepository.findById(idFile).get();
    }
    @Override
    @Transactional
    public List<File> getFilesByFolder(Long folderId) {
        return filerepository.findByFolderId(folderId);
    }

    @Override
    public void deleteFileById(Long idFilde) {
        filerepository.deleteById(idFilde);
    }
    @Override
    @Transactional
    public String addContractToFolder( MultipartFile file) throws IOException {
        String contracts="Contracts";
        String folderId=folderService.addFolderByName(contracts);

        String fileId = alfrescoService.addFileToFolder(contracts, file);
        String fileName = file.getOriginalFilename();

        Folder folder = folderrepository.findByPath(folderId)
                .orElseThrow(() -> new NotFoundException("Folder not found: " + folderId));


        File fileEntity = new File();
        fileEntity.setName(fileName);
        fileEntity.setPath(fileId); // Assuming fileId from Alfresco can be used as path
        fileEntity.setCreationDate(new Date());
        fileEntity.setFolder(folder);
        fileEntity = filerepository.save(fileEntity);

        return fileEntity.getIdFile().toString();     }
}
