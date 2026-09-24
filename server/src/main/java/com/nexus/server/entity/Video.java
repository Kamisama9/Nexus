package com.nexus.server.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    @Column(columnDefinition = "TEXT")
    private String filePath;
    @Column(columnDefinition = "TEXT")
    private String overView;
    private String posterPath;
    private String releaseDate;
    private Long size;
    private LocalDateTime modifiedDate;

    // @OneToOne(mappedBy = "video", cascade = CascadeType.ALL)
    // private UserProgress userProgress; //One to one relationship with
    // UserProgress entity --> no need of bidirectional approach right now, as we
    // are not using it anywhere in the code.
}
