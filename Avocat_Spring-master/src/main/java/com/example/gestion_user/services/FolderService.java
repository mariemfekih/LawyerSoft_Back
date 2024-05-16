package com.example.gestion_user.services;

import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.models.request.FolderDto;

import java.util.List;

public interface FolderService {
    Folder addFolder(FolderDto f, Long caseId);
    //public Folder updateFolder( Long id,FolderDto folder);
    Folder updateFolder(Long id, FolderDto updatedFolderDto);
    public void deleteFolder(Long idFolder);
    public List<Folder> getFolders();
    public Folder getFolderByName(String name);
    public Folder getFolderById(Long idFolder);


    //public List<String> getNomsDossiers();
    //  public Dossier ajouterDossier(Dossier dossier, Integer idPhase, List<Fichier> files);// shoufha


}
