package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.File;
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
    public File addFile(File file) {
        return filerepository.save(file);
    }

    @Override
    public List<File> getFiles() {
        return filerepository.findAll();
    }

    @Override
    public File updateFile(File file) {
        return filerepository.save(file);
    }

    @Override
    public void deleteFile(Integer idFile) {
        filerepository.deleteById(idFile);
    }

    @Override
    public File getFileById(Integer idFile) {
        return filerepository.findById(idFile).get();
    }
}
