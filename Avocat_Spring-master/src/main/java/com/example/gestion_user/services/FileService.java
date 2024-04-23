package com.example.gestion_user.services;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.models.request.FileDto;

import java.util.List;

public interface FileService {
    File addFile (FileDto file) ;
    File updateFile (Long id,FileDto file) ;

    void deleteFile (Long idFile) ;

    List<File> getFiles() ;

    File getFileById (Long idFile);
}
