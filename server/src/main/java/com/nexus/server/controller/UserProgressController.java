package com.nexus.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nexus.server.dto.VideoProgressDTO;
import com.nexus.server.entity.UserProgress;
import com.nexus.server.service.UserProgressService;


@Controller
@RequestMapping("/api/v1/user-progress") 
@CrossOrigin(origins = "http://localhost:5173")
public class UserProgressController {

    @Autowired 
    private UserProgressService userProgressService;

     @GetMapping("{id}")
    public ResponseEntity<UserProgress> getUserProgress(@PathVariable  Long id){
        UserProgress userProgress = userProgressService.getUserProgress(id);
        return new ResponseEntity<>(userProgress , HttpStatus.OK);
    }

    @PutMapping("{id}/progress")
    public ResponseEntity<String> updateUserProgress(@PathVariable Long id ,@RequestBody VideoProgressDTO progress){
        userProgressService.updateUserProgress(id,progress);
        return new ResponseEntity<>("saved", HttpStatus.OK);
    }
}
