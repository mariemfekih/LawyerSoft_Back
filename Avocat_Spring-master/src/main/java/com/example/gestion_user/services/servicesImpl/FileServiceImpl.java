package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FileDto;
import com.example.gestion_user.repositories.FileRepository;
import com.example.gestion_user.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    FileRepository filerepository;

    @Override
    public File addFile(FileDto f) {
        File file=new File();
        file.setName(f.getName());
        file.setPath(f.getPath());
        try {
            return filerepository.save(file);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create court: " + ex.getMessage());
        }
    }
    @Override
    public File updateFile(Long id, FileDto updatedFileDto) {
        // Find the existing File entity by ID
        File existingFile = filerepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File not found with id: " + id));

        // Update the fields of the existing File entity with values from the DTO
        existingFile.setName(updatedFileDto.getName());
        existingFile.setPath(updatedFileDto.getPath());

        // Save the updated File entity
        try {
            return filerepository.save(existingFile);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update file with id: " + id + ". " + ex.getMessage());
        }
    }


    @Override
    public void deleteFile(Long idFile) {
        filerepository.deleteById(idFile);
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
