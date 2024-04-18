package com.example.gestion_user.services;

import com.example.gestion_user.entities.File;

import java.util.List;

public interface FileService {
    File addFile (File file) ;

    List<File> getFiles() ;

    File updateFile (File file) ;

    void deleteFile (Integer idFile) ;

    File getFileById (Integer idFile);
}
