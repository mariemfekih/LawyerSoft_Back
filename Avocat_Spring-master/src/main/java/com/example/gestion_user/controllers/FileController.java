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

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/File")
public class FileController {
    @Autowired
    FileService fileService;


    @PostMapping
    public ResponseEntity<File> addFile(@RequestBody FileDto file) {
        File addedFile = fileService.addFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<File> updateFile(@PathVariable Long id,@RequestBody FileDto updatedFile) {
        File existingFile = fileService.getFileById(id);
        if (existingFile == null) {
            return ResponseEntity.notFound().build();
        }
        File file = fileService.updateFile(id,updatedFile);
        return ResponseEntity.ok(file);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idFile}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long idFile) {
        fileService.deleteFile(idFile);
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
