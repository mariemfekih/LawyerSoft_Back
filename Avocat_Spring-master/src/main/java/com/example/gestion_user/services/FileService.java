package com.example.gestion_user.services;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.models.request.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File addFile(Long folderId, MultipartFile multipartFile);
    //File updateFile (Long id,FileDto file) ;

    public void deleteFile(Long idFile,Long idFolder) ;

    List<File> getFiles() ;

    File getFileById (Long idFile);
}
