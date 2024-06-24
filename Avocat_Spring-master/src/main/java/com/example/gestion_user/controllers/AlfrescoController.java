package com.example.gestion_user.controllers;

import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.services.AlfrescoService;
import lombok.RequiredArgsConstructor;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.impl.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/folders")
public class AlfrescoController {

    private final AlfrescoService alfrescoService;


    @PutMapping("/{folderId}/updateFileName/{newName}")
    public ResponseEntity<String> updateFileName(@PathVariable("folderId") String folderId,
                                                 @PathVariable("newName") String newName) {
        try {
            // Update file name in Alfresco
            alfrescoService.updateFileName(folderId, newName);

            return ResponseEntity.ok("Folder name updated successfully in Alfresco.");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update folder name in Alfresco: " + e.getMessage());
        }
    }
    @PostMapping("/{path}")
    public ResponseEntity<String> addFolder(@PathVariable("path") String path)
    {
        String nameFolder=this.alfrescoService.addFolder(path);
        return ResponseEntity.accepted().body(nameFolder);

    }

    @PutMapping("/{currentName}/{newName}")
    public ResponseEntity<Void> updateFolderName(
            @PathVariable("currentName") String currentName,
            @PathVariable("newName") String newName) {
        this.alfrescoService.updateFolderName(currentName, newName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{folderName}/files")
    public ResponseEntity<String> addFileToFolder(@PathVariable("folderName") String folderName,
                                                  @RequestParam("file") MultipartFile file) throws IOException {

        String fileId = this.alfrescoService.addFileToFolder(folderName, file);
        return ResponseEntity.accepted().body(fileId);
    }

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<?> deleteFileById(@PathVariable String fileId) {
        try {
            alfrescoService.deleteFileById(fileId);
            return ResponseEntity.ok("File deleted successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file: " + e.getMessage());
        }
    }

    @GetMapping("/profile-image/{fileId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String fileId) {
        try {
            Document document = alfrescoService.getFileById(fileId);
            ContentStream contentStream = document.getContentStream();
            byte[] imageBytes = contentStream.getStream().readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(document.getContentStreamMimeType()));
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new NotFoundException("Image not found: " + e.getMessage());
        }
    }
    @GetMapping("/document/{fileId}")
    public ResponseEntity<byte[]> getDocumentById(@PathVariable String fileId) {
        try {
            if (fileId == null || fileId.isEmpty()) {
                throw new IllegalArgumentException("Invalid fileId: " + fileId);
            }

            Document document = alfrescoService.getFileById(fileId);
            if (document == null) {
                throw new NotFoundException("Document not found for fileId: " + fileId);
            }

            ContentStream contentStream = document.getContentStream();
            byte[] documentBytes = contentStream.getStream().readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(document.getContentStreamMimeType()));
            headers.setContentLength(documentBytes.length);
            headers.setContentDisposition(ContentDisposition.builder("inline").filename(document.getName()).build());

            return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new NotFoundException("Document not found: " + e.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
