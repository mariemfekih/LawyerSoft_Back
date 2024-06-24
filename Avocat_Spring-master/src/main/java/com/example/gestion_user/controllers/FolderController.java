package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.Fee;
import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FolderDto;
import com.example.gestion_user.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("Folder")
public class FolderController {

    @Autowired
    FolderService folderService;

    @PostMapping("/{customerId}")
    public ResponseEntity<String> addFolder( @PathVariable Long customerId) {
        String addedFolder = folderService.addFolder(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFolder);
    }
/*    @PutMapping("/{path}/updateFileName/{newName}")
    public ResponseEntity<String> updateFileNameByPath(@PathVariable("path") String path,
                                                       @PathVariable("newName") String newName) {
        try {
            folderService.updateFolderName(path, newName);
            return ResponseEntity.ok("File name updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update file name: " + e.getMessage());
        }
    }*/
@PutMapping("/{path}/updateFileName/{newName}")
public ResponseEntity<Map<String, String>> updateFileNameByPath(@PathVariable("path") String path,
                                                                @PathVariable("newName") String newName) {
    try {
        folderService.updateFolderName(path, newName);
        Map<String, String> response = new HashMap<>();
        response.put("message", "File name updated successfully");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Failed to update file name: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{path}")
    public void deleteFolder(@PathVariable("path") String path) {
        folderService.deleteFolderByPath(path);
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

    @GetMapping("/name/{name}")
    public ResponseEntity<Folder> getFolderByName(@PathVariable String name) {
        Folder folder = folderService.getFolderByName(name);
        if (folder != null) {
            return ResponseEntity.ok().body(folder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/addSubfolder/{parentFolderId}/{caseId}")
    public ResponseEntity<?> addSubFolder(@PathVariable Long parentFolderId, @PathVariable Long caseId) {
        try {
            Folder subFolder = folderService.addSubFolder(caseId, parentFolderId);
            return ResponseEntity.ok(subFolder); // Return the created sub-folder
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add sub-folder: " + e.getMessage());
        }
    }

    @GetMapping("/{parentFolderId}/subfolders")
    public ResponseEntity<List<Folder>> getSubfolders(@PathVariable Long parentFolderId) {
        List<Folder> subfolders = folderService.getSubfolders(parentFolderId);
        if (subfolders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subfolders);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Folder>> getFoldersByUserId(@PathVariable Long userId) {
        try {
            List<Folder> folders = folderService.getFoldersByUserId(userId);
            return ResponseEntity.ok(folders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/path/{folderPath}")
    public ResponseEntity<?> getFolderIdByPath(@PathVariable String folderPath) {
        Long fileId = folderService.getIdByPath(folderPath);
        if (fileId != null) {
            return ResponseEntity.ok("File ID: " + fileId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found for path: " + folderPath);
        }
    }

}
