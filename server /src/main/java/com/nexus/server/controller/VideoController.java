package com.nexus.server.controller;



import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.server.entity.Video;
import com.nexus.server.repository.VideoRepository;
import org.springframework.web.bind.annotation.GetMapping;





@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class VideoController{

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/play/{id}")
    public ResponseEntity<Resource> videoPlayer(@PathVariable Long id){
        Video newVideo = videoRepository.findById(id).orElse(null);
        // newVideo is a object and i need to return a resource
        // newVideo.getFilePath is a string but UrlResource need a uri like this "file:///D:/movies/matrix.mp4"
        if(newVideo == null )
            return ResponseEntity.notFound().build();
        try {
        Path path=Paths.get(newVideo.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        
        


    }
}