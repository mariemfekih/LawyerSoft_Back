package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Fee;
import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.models.request.FolderDto;
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

    @PostMapping
    public ResponseEntity<Folder> addFolder(@RequestBody FolderDto f) {
        Folder addedFolder = folderService.addFolder(f);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFolder);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long id,@RequestBody FolderDto updatedFolder) {
        Folder existingFolder = folderService.getFolderById(id);
        if (existingFolder == null) {
            return ResponseEntity.notFound().build();
        }
        Folder folder = folderService.updateFolder(id,updatedFolder);
        return ResponseEntity.ok().body(folder);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idFolder}")
    public void deleteFolder(@PathVariable("idFolder") Long idFolder) {
        folderService.deleteFolder(idFolder);
    }

    @GetMapping
    public ResponseEntity<List<Folder>> getFolders() {
        List<Folder> folders = folderService.getFolders();
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/{idFolder}")
    public Folder getFolderById(@PathVariable("idFolder") Long idFolder) {
        return folderService.getFolderById(idFolder);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Folder> getFolderByName(@PathVariable String name) {
        Folder folder = folderService.getFolderByName(name);
        if (folder != null) {
            return ResponseEntity.ok().body(folder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
