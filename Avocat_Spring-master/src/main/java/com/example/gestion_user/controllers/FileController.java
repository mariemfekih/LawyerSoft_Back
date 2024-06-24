package com.example.gestion_user.controllers;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.entities.Folder;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.models.request.FileDto;
import com.example.gestion_user.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/File")
public class FileController {
    @Autowired
    FileService fileService;


    @PostMapping("/folders/{folderId}/files")
    public ResponseEntity<String> addFileToFolder(@PathVariable Long folderId,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.addFileToFolder(folderId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(fileId); // Return the ID of the uploaded file
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add file: " + e.getMessage());
        } catch ( IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }

    @PutMapping("/{path}/updateFileName/{newName}")
    public ResponseEntity<Map<String, String>> updateFileNameByPath(@PathVariable("path") String path,
                                                                    @PathVariable("newName") String newName) {
        Map<String, String> response = new HashMap<>();
        try {
            fileService.updateFileName(path, newName);
            response.put("message", "File name updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to update file name: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{path}")
    public ResponseEntity<Void> deleteFileByPath(@PathVariable String path) {
        try {
            fileService.deleteFileByPath(path);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/path/{filePath}")
    public ResponseEntity<?> getFileIdByPath(@PathVariable String filePath) {
        Long fileId = fileService.getIdByPath(filePath);
        if (fileId != null) {
            return ResponseEntity.ok("File ID: " + fileId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found for path: " + filePath);
        }
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
    @GetMapping("/Folder/{folderId}")
    public ResponseEntity<List<File>> getFilesByFolder(@PathVariable Long folderId) {
        List<File> files = fileService.getFilesByFolder(folderId);
        if (files.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(files);
    }
    @PostMapping("/addContract")
    public ResponseEntity<String> addContractToFolder(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.addContractToFolder(file);
            return ResponseEntity.ok("File uploaded successfully. File ID: " + fileId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

}
