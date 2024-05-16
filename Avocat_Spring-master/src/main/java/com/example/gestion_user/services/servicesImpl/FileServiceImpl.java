package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FileDto;
import com.example.gestion_user.repositories.FileRepository;
import com.example.gestion_user.repositories.FolderRepository;
import com.example.gestion_user.services.AlfrescoService;
import com.example.gestion_user.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileRepository filerepository;
    @Autowired
    FolderRepository folderrepository;
    @Autowired
    private AlfrescoService alfrescoService;
    @Override
    public File addFile(Long folderId, MultipartFile multipartFile) {
        // Retrieve the folder from the database
        Folder folder = folderrepository.findById(folderId)
                .orElseThrow(() -> new NotFoundException("Folder not found"));
        // Save the file to Alfresco
        String alfrescoFileId;
        try {
            alfrescoFileId = alfrescoService.addFileToFolder(folder.getName(), multipartFile);
        } catch (IOException e) {
            throw new NotFoundException("Failed to upload file to Alfresco: " + e.getMessage());
        }
        // Create a new file object and save it to the local database
        File file = new File();
        file.setName(multipartFile.getOriginalFilename());
        file.setPath(alfrescoFileId); // Store Alfresco file ID or URL
        file.setCreationDate(new Date());
        file.setFolder(folder);
        return filerepository.save(file);
    }
//    @Override
//    public File updateFile(Long id, FileDto updatedFileDto) {
//        // Find the existing File entity by ID
//        File existingFile = filerepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("File not found with id: " + id));
//
//        // Update the fields of the existing File entity with values from the DTO
//        existingFile.setName(updatedFileDto.getName());
//        existingFile.setPath(updatedFileDto.getPath());
//
//        // Save the updated File entity
//        try {
//            return filerepository.save(existingFile);
//        } catch (Exception ex) {
//            throw new NotFoundException("Failed to update file with id: " + id + ". " + ex.getMessage());
//        }
//    }


    @Transactional
    @Override
    public void deleteFile(Long idFile, Long idFolder) {
        Folder folder = folderrepository.findById(idFolder)
                .orElseThrow(() -> new NotFoundException("Folder not found"));
        File file = filerepository.findById(idFile)
                .orElseThrow(() -> new NotFoundException("File not found"));
        filerepository.deleteById(idFile);
        alfrescoService.deleteFileByName(folder.getName(), file.getName());
    }
    @Override
    public List<File> getFiles() {
        return filerepository.findAll();
    }



    @Override
    public File getFileById(Long idFile) {
        return filerepository.findById(idFile).get();
    }
}
