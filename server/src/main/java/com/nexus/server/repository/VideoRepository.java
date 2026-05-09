package com.nexus.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexus.server.entity.Video;

public interface VideoRepository  extends JpaRepository<Video,Long>{
    
}
