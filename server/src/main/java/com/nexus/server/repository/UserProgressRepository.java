package com.nexus.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexus.server.entity.UserProgress;

@Repository 
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

}