package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.File;
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

    @GetMapping("/getFiles")
    public ResponseEntity<List<File>> getAllFiles() {
        List<File> files = fileService.getFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/getFileById/{idFile}")
    public ResponseEntity<File> getFileById(@PathVariable Integer idFile) {
        File file = fileService.getFileById(idFile);
        if (file!=null) {
            return ResponseEntity.ok(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addFile")
    public ResponseEntity<File> addFile(@RequestBody File file) {
        File addedFile = fileService.addFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedFile);
    }

    @PutMapping("/updateFile")
    public ResponseEntity<File> updateFile(@RequestBody File file) {
        File updatedFile = fileService.updateFile(file);
        return ResponseEntity.ok(updatedFile);
    }

    @DeleteMapping("/deleteFile/{idFile}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer idFile) {
        fileService.deleteFile(idFile);
        return ResponseEntity.noContent().build();
    }
}
