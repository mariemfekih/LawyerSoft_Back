package com.example.gestion_user.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("")
public class ThumbnailController {

    private static final String THUMBNAILS_DIRECTORY = "thumbnails/";

    @GetMapping("/getThumbnail/{filename:.+}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable String filename) {
        try {
            Path thumbnailPath = Paths.get(THUMBNAILS_DIRECTORY, filename);

            if (!Files.exists(thumbnailPath)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            byte[] thumbnailBytes = Files.readAllBytes(thumbnailPath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(thumbnailBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}


