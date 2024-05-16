package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.models.request.FileDto;
import com.example.gestion_user.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/File")
public class FileController {
    @Autowired
    FileService fileService;


    @PostMapping("/folders/{folderId}/files")
    public ResponseEntity<File> addFile(@PathVariable Long folderId,
                                        @RequestParam("file") MultipartFile multipartFile) {
        File addedFile = fileService.addFile(folderId, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFile);
    }




    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/folders/{idFolder}/files/{idFile}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long idFile, @PathVariable Long idFolder) {
        fileService.deleteFile(idFile, idFolder);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<File>> getAllFiles() {
        List<File> files = fileService.getFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{idFile}")
    public ResponseEntity<File> getFileById(@PathVariable Long idFile) {
        File file = fileService.getFileById(idFile);
        if (file!=null) {
            return ResponseEntity.ok(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
