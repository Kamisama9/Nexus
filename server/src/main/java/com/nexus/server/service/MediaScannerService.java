package com.nexus.server.service;

import com.nexus.server.repository.UserProgressRepository;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
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

    private final UserProgressRepository userProgressRepository;

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

    MediaScannerService(UserProgressRepository userProgressRepository) {
        this.userProgressRepository = userProgressRepository;
    }

    public List<Video> changeLibrary(Path file){
        videoRepository.deleteAll();
        userProgressRepository.deleteAll();
        return scanManager(file);
    }

    public List<Video> scanManager(Path file) {
        // use businessKey to find the existing files in the DB and update them before
        // scanning the new files
        // videoRepository.deleteAll();
        File root = file.toFile();
        fileScanner(root);
        return videoRepository.findAll();
    }

    public void fileScanner(File root) {
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
                    fileScanner(f);
                } else {
                    checkFile(f);
                }
            }
        } else {
            checkFile(root);
        }
    }

    public void checkFile(File file) {
        String videoString = file.getName().toLowerCase();
        if (videoString.endsWith(".mp4") || videoString.endsWith(".mkv") || videoString.endsWith(".avi")
                || videoString.endsWith(".mov")) {
            Video existingVideo = findExistingVideo(file);
            if (existingVideo != null) {
                existingVideo.setFilePath(file.getAbsolutePath());
                videoRepository.save(existingVideo);
            } else {
                saveFile(file);
            }
        }
    }

    public Video findExistingVideo(File file) {
        Path path = file.toPath();
        try {
            Long size = Files.size(path);
            LocalDateTime lastModifiedTime = Files.getLastModifiedTime(path).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            Video video = videoRepository.findBySizeAndModifiedDate(size, lastModifiedTime);
            if (video != null) {
                return video;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MovieMetadata getTmdbMetadata(String videoName) {
        MovieMetadata metadata = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(videoName);
            videoName = jsonNode.get("title").asText();
            String year = jsonNode.get("year").asText();

            metadata = tmdbService.getDetails(videoName);
        } catch (Exception e) {
            e.printStackTrace();
            metadata = tmdbService.getDetails(videoName);
        }
        if (metadata != null) {
            System.out.println("Found Poster: " + metadata.posterUrl());
            System.out.println("Found Plot: " + metadata.overview());
        }
        return metadata;
    }

    public void saveFile(File videoFile) {
        String videoString = videoFile.getName();
        String videoName = nameParser(videoString);

        // get clean name from ai
        String cleanName = aiService.cleanNameWithAi(videoName);
        System.out.print(cleanName);
        MovieMetadata metadata = getTmdbMetadata(cleanName);

        try {
            Video v = new Video();
            v.setFileName(metadata != null ? metadata.title() : cleanName);
            v.setFilePath(videoFile.getAbsolutePath());
            v.setPosterPath(metadata != null ? metadata.posterUrl() : null);
            v.setOverView(metadata != null ? metadata.overview() : null);

            Long size = Files.size(videoFile.toPath());
            // convert fileTime to localDateTime
            LocalDateTime lastModifiedTime = Files.getLastModifiedTime(videoFile.toPath()).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
            v.setSize(size);
            v.setModifiedDate(lastModifiedTime);
            videoRepository.save(v);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String nameParser(String fileName) {
        // remove the extension from the file name
        fileName = fileName.toLowerCase();
        String name = fileName.replaceAll("\\.mp4|\\.mkv|\\.avi|\\.mov", "");
        // remove qulaity from the file name
        name = name.replaceAll("(?i)1080p|720p|bluray|x264|x265|hevc", "");
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
        return videoRepository.findByFileNameContainingIgnoreCase(keyword);
    }
}