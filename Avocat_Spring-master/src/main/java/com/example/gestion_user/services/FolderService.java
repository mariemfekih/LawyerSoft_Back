package com.example.gestion_user.services;

import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.models.request.FolderDto;

import java.util.List;

public interface FolderService {
    String addFolder( Long caseId);
    //public Folder updateFolder( Long id,FolderDto folder);
    void updateFolderName(String path, String newName);
    void deleteFolderByPath(String path);
    Long getIdByPath(String path);
    public List<Folder> getFolders();
    public Folder getFolderByName(String name);
    public Folder getFolderById(Long idFolder);

  Folder addSubFolder(Long caseId, Long parentFolderId);
    List<Folder> getSubfolders(Long parentFolderId);
    List<Folder> getFoldersByUserId(Long userId);
    String addFolderByName(String folderName);

}
