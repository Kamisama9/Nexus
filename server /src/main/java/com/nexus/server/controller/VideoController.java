package com.nexus.server.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexus.server.dto.VideoListResponse;
import com.nexus.server.service.VideoService;

import org.springframework.web.bind.annotation.GetMapping;





@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1")
public class VideoController{

    @Autowired
    private VideoService videoService;

    @GetMapping("/play/{id}")
    public ResponseEntity<Resource> videoPlayer(@PathVariable Long id){
        Resource resource=videoService.videoPlayer(id);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity<List<VideoListResponse>> getAllVideos(){
        List<VideoListResponse> videos =videoService.getAllVideos();
        return new ResponseEntity<>(videos , HttpStatus.OK);    
    }
}