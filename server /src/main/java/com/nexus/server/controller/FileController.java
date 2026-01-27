package com.nexus.server.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nexus.server.dto.FilePathRequestDTO;
import com.nexus.server.repository.VideoRepository;
import com.nexus.server.service.MediaScannerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

    @Autowired
    public MediaScannerService mediaScannerService;

    @Autowired
    public VideoRepository videoRepository;

    @GetMapping("test")
    public String test() {
        return new String("Hello");
    }

    // Get file path from frontend
    @PostMapping("path")
    public ResponseEntity<Object> processFilePath(@RequestBody FilePathRequestDTO request) {
        // use the file path and search for the file
        String pathString = request.getFilePath();

        // path cannot be empty
        if (pathString == null || pathString.isEmpty()) {
            return new ResponseEntity<>("Path cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Path file = Paths.get(pathString);
        if (Files.exists(file)) {
            

            mediaScannerService.scanManager(file);

            return new ResponseEntity<>(videoRepository.findAll(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Folder Not Found", HttpStatus.NOT_FOUND);
        }
    }
}
