package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class FolderController {

    @Autowired
    FolderService folderService;

    @GetMapping("/getFolders")
    public ResponseEntity<List<Folder>> getFolders() {
        List<Folder> folders = folderService.getFolders();
        return ResponseEntity.ok(folders);
    }



    @GetMapping("/getFolderById/{idFolder}")
    public Folder getFolderById(@PathVariable("idFolder") Integer idFolder) {
        return folderService.getFolderById(idFolder);
    }


    @DeleteMapping("/deleteFolder/{idFolder}")
    public void deleteFolder(@PathVariable("idFolder") Integer idFolder) {
        folderService.deleteFolder(idFolder);
    }
    @PostMapping("/addFolder")
    public ResponseEntity<Folder> addFolder(@RequestBody Folder f) {
        Folder addedFolder = folderService.addFolder(f);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFolder);
    }


    @PutMapping("/updateFolder")
    public ResponseEntity<Folder> updateFolder(@RequestBody Folder f) {
        Folder updatedFolder = folderService.updateFolder(f);
        return ResponseEntity.ok().body(updatedFolder);
    }
    @GetMapping("getFolderByName/{name}")
    public ResponseEntity<Folder> getFolderByName(@PathVariable String name) {
        Folder folder = folderService.getFolderByName(name);
        if (folder != null) {
            return ResponseEntity.ok().body(folder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


   /* @PostMapping("/add-dossier")
    public void addDossier(@RequestBody Dossier dossier) {
        dossierService.addDossier(dossier);
    }



    @GetMapping("/get-noms-dossiers")
    public List<String> listDesNomsDossiers() {
        return folderService.getNomsDossiers();
    }*/
}
