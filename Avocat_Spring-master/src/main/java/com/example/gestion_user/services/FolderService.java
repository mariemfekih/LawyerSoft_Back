package com.example.gestion_user.services;

import com.example.gestion_user.entities.Folder;

import java.util.List;

public interface FolderService {

    public List<Folder> getFolders();
    public Folder getFolderById(Integer idFolder);
    public Folder getFolderByName(String name);
    public Folder updateFolder( Folder folder);
    public void deleteFolder(Integer idFolder);
    public Folder addFolder(Folder f);

    //public List<String> getNomsDossiers();
    //  public Dossier ajouterDossier(Dossier dossier, Integer idPhase, List<Fichier> files);// shoufha


}
