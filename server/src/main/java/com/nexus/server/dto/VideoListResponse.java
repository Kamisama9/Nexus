package com.nexus.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoListResponse{
    public long id;
    public String fileName;
    public String overView;
    public String posterPath;
    public String releaseDate;
}