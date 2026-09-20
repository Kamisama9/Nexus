package com.nexus.server.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexus.server.dto.VideoProgressDTO;
import com.nexus.server.entity.UserProgress;
import com.nexus.server.entity.Video;
import com.nexus.server.repository.UserProgressRepository;
import com.nexus.server.repository.VideoRepository;

@Service
public class UserProgressService {

    @Autowired 
    private UserProgressRepository UserProgressRepository;

    @Autowired
    private VideoRepository videoRepository;




     public UserProgress getUserProgress(Long videoId) {
        return UserProgressRepository.findByVideo_Id(videoId);
    }

    public void updateUserProgress(Long videoId,VideoProgressDTO progress) {
        UserProgress userProgress = UserProgressRepository.findByVideo_Id(videoId);

        Video video = videoRepository.findById(videoId).orElse(null);

        System.out.println("saved time " + progress.progress);
        if (userProgress == null) {
            userProgress = new UserProgress(); 
            userProgress.setProgress(progress.progress);
            userProgress.setUpdatedAt(LocalDateTime.now());   
            userProgress.setVideo(video);
        }else{
            userProgress.setProgress(progress.progress);
            userProgress.setUpdatedAt(LocalDateTime.now());
        }
        UserProgressRepository.save(userProgress);
    }
}
