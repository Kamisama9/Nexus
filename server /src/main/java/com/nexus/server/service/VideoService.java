package com.nexus.server.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.nexus.server.dto.VideoListResponse;
import com.nexus.server.entity.Video;
import com.nexus.server.repository.VideoRepository;

@Service
public class VideoService {
    @Autowired
    private  VideoRepository videoRepository;

    public Resource videoPlayer(Long id){
        Video newVideo = videoRepository.findById(id).orElse(null);
        // newVideo is a object and i need to return a resource
        // newVideo.getFilePath is a string but UrlResource need a uri like this "file:///D:/movies/matrix.mp4"
        if(newVideo == null )
            return null;
        try {
        Path path=Paths.get(newVideo.getFilePath());
        Resource resource = new UrlResource(path.toUri());
        return resource;
        } catch (Exception e) {
            return null;
        }
    }

    public List<VideoListResponse> getAllVideos(){
        List<Video> ls=videoRepository.findAll();
        List<VideoListResponse> videos = new ArrayList<VideoListResponse>();
        for(Video v: ls ){
            VideoListResponse video=new VideoListResponse();
            video.setId(v.getId());
            video.setName(v.getFileName());
            videos.add(video);
        }
        return videos;
    }
}
