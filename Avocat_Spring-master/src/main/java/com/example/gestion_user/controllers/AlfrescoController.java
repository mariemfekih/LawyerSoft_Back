package com.example.gestion_user.controllers;

import com.example.gestion_user.services.AlfrescoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/folders")
public class AlfrescoController {

    private final AlfrescoService alfrescoService;



    @PostMapping("/{name}")
    public ResponseEntity<String> addFolder(@PathVariable("name") String name)
    {
        String nameFolder=this.alfrescoService.createFolder(name);
        return ResponseEntity.accepted().body(nameFolder);

    }

    @PutMapping("/{currentName}/{newName}")
    public ResponseEntity<Void> updateFolderName(
            @PathVariable("currentName") String currentName,
            @PathVariable("newName") String newName) {
        this.alfrescoService.updateFolderName(currentName, newName);
        return ResponseEntity.noContent().build();
    }



    @DeleteMapping("/{folderName}")
    public ResponseEntity<Void> deleteFolder(@PathVariable("folderName") String folderName) {
        this.alfrescoService.deleteFolder(folderName);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{folderName}/files")
    public ResponseEntity<String> addFileToFolder(@PathVariable("folderName") String folderName,
                                                  @RequestParam("file") MultipartFile file) throws IOException {

        String fileId = this.alfrescoService.addFileToFolder(folderName, file);
        return ResponseEntity.accepted().body(fileId);
    }
    @DeleteMapping("/{folderName}/files/{filename}")
    public ResponseEntity<Void> deleteFile(@PathVariable("folderName") String folderName, @PathVariable("filename") String fileName) throws IOException {
        this.alfrescoService.deleteFileByName(folderName,fileName);
        return ResponseEntity.noContent().build();
}
}
