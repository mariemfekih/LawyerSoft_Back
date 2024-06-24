package com.example.gestion_user.services;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.models.request.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String addFileToFolder(Long folderId, MultipartFile file) throws IOException;
    void deleteFileByPath(String fileId);
    List<File> getFiles() ;
    void updateFileName(String fileId, String newName);

   Long getIdByPath(String path) ;
    File getFileById (Long idFile);
    List<File> getFilesByFolder(Long folderId);
    void deleteFileById(Long idFilde);
    String addContractToFolder( MultipartFile file) throws IOException;}
