package com.nexus.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexus.server.entity.Video;

@Repository 
public interface VideoRepository  extends JpaRepository<Video,Long>{
    List<Video> findByFileNameContainingIgnoreCase(String keyword);
    @Query("SELECT v FROM Video v where v.size=:size AND v.modifiedDate=:modifiedDate")
    Video findBySizeAndModifiedDate(@Param("size") Long size,@Param("modifiedDate") LocalDateTime modifiedDate);
    @Query("SELECT v.id FROM Video v")
    List<Long> findAllVideoIds();
}
