package com.nexus.server.service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.server.dto.MovieMetadata;
import com.nexus.server.entity.Video;
import com.nexus.server.repository.VideoRepository;

@Service
public class MediaScannerService {

    private static final Set<String> IGNORE_FOLDERS = new HashSet<>(Arrays.asList(
            "$recycle.bin", "system volume information", "windows", "programdata",
            "recovery", "perflogs", "appdata",

            "program files", "program files (x86)",

            "node_modules", ".git", ".idea", ".vscode", "target", "build", "dist", "venv",

            "temp", "tmp", ".cache"));

    // store the files in the DB
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private AiService aiService;

    public List<Video> scanManager(Path file) {
        videoRepository.deleteAll();
        File root = file.toFile();
        FileScanner(root);
        return videoRepository.findAll();
    }

    public void FileScanner(File root) {
        // receive file from controller and scan the file
        // the recieved file is a Path file convert it to a file

        if (root.isDirectory()) {
            File[] allFiles = root.listFiles();
            if (allFiles == null)
                return;
            for (File f : allFiles) {
                if (IGNORE_FOLDERS.contains(f.getName().toLowerCase())) {
                    continue;
                }
                if (f.isDirectory()) {
                    FileScanner(f);
                } else {
                    saveFile(f);
                }
            }
        } else {
            saveFile(root);
        }
    }

    public void saveFile(File videoFile) {
        String videoString = videoFile.getName();
        String videoName = nameParser(videoString);

        // get clean name from ai
        String cleanName = aiService.cleanNameWithAi(videoName);

        MovieMetadata metadata = null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.readTree(cleanName);

            videoName = jsonNode.get("title").asText();
            String year = jsonNode.get("year").asText();


            metadata =tmdbService.getDetails(videoName);

        } catch (Exception e) {
            e.printStackTrace();
            metadata = tmdbService.getDetails(videoName);
        }
        if(metadata != null) {
           System.out.println("Found Poster: " + metadata.posterUrl());
            System.out.println("Found Plot: " + metadata.overview());
        }

        if (videoString.endsWith(".mp4") || videoString.endsWith(".mkv") || videoString.endsWith(".avi")
                || videoString.endsWith(".mov")) {
            Video v = new Video();
            v.setFileName(videoName);
            v.setFilePath(videoFile.getAbsolutePath());
            v.setPosterPath(metadata != null ? metadata.posterUrl() : null);
            v.setOverView(metadata != null ? metadata.overview() : null);
            videoRepository.save(v);
        }
    }

    public String nameParser(String fileName) {
        // remove the extension from the file name
        String name = fileName.replaceAll("\\.mp4|\\.mkv|\\.avi|\\.mov", "");
        // remove qulaity from the file name
        name = name.replaceAll("1080p|720p|BluRay|x264|x265|HEVC", "");
        // remove brackets from the file name
        // .* means "match absolutely everything between the brackets"
        // If you had the string [YTS] The Matrix [1080p], a greedy regex would start at
        // the very first [ and not stop until the very last ]
        // ? makes it non-greedy, so it would match [YTS] and [1080p] separately.
        name = name.replaceAll("\\[.*?\\]", " ");
        name = name.replaceAll("\\(.*?\\)", " ");
        return name;
    }

    public List<Video> SearchVideo(String keyword) {
        List<Video> videos = new ArrayList<>();
        Video video = videoRepository.findByFileNameContainingIgnoreCase(keyword);
        videos.add(video);
        return videos;
    }
}