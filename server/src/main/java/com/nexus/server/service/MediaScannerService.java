package com.nexus.server.service;

import java.io.File;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexus.server.entity.Video;
import com.nexus.server.repository.VideoRepository;

@Service
public class MediaScannerService {
    // store the files in the DB
    @Autowired
    private VideoRepository videoRepository;

    public void scanManager(Path file){
        videoRepository.deleteAll();
        File root=file.toFile();
        FileScanner(root);
    }

    public void FileScanner(File root) {
        // receive file from controller and scan the file
        // the recieved file is a Path file convert it to a file

        if (root.isDirectory()) {
            File[] allFiles = root.listFiles();
            if (allFiles == null)
                return;
            for (File f : allFiles) {
                if (f.isDirectory()) {
                    FileScanner(f);
                } else {
                    saveFile(f);
                }
            }
        } else {
            saveFile(root); 
        }
    }

    public void saveFile(File videoFile) {
        String videoString = videoFile.getName();
        if (videoString.endsWith(".mp4")) {
            Video v = new Video();
            v.setFileName(videoFile.getName());
            v.setFilePath(videoFile.getAbsolutePath());
            videoRepository.save(v);
        }
    }
}
