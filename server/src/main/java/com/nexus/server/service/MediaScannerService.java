package com.nexus.server.service;

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
import com.nexus.server.entity.UserProgress;
import com.nexus.server.entity.Video;
import com.nexus.server.repository.UserProgressRepository;
import com.nexus.server.repository.VideoRepository;

@Service
public class MediaScannerService {

    private static final Set<String> IGNORE_FOLDERS = new HashSet<>(Arrays.asList(
            "$recycle.bin",
            "system volume information",
            "windows",
            "windowsapps",
            "config.msi",
            "programdata",
            "recovery",
            "perflogs",
            "appdata",
            "program files",
            "program files (x86)",
            "node_modules",
            ".git",
            ".idea",
            ".vscode",
            "target",
            "build",
            "dist",
            "venv",
            "temp",
            "tmp",
            ".cache"));

    // store the files in the DB
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private AiService aiService;

    @Autowired
    private UserProgressRepository userProgressRepository;

    public List<Video> changeLibrary(Path file) {
        userProgressRepository.deleteAll();
        videoRepository.deleteAll();
        return scanManager(file);
    }

    public void deleteStales(Set<Long> existingVideoIds) {
        for (Long videoId : existingVideoIds) {
            Video video = videoRepository.findById(videoId).orElse(null);
            if (video != null) {
                File file = new File(video.getFilePath());
                if (!file.exists()) {
                    userProgressRepository.deleteByVideo_Id(videoId);
                    videoRepository.delete(video);
                }
            }
        }
    }

    public List<Video> scanManager(Path file) {
        File root = file.toFile();
        Set<Long> existingVideoIds = new HashSet<>();
        // Only take a snapshot when scanning a directory
        if (root.isDirectory()) {
            existingVideoIds = new HashSet<>(videoRepository.findAllVideoIds());
        }
        boolean scanSuccessful = fileScanner(root);
        System.out.print(scanSuccessful);
        // Only a complete directory scan can tell us which files disappeared
        if (root.isDirectory() && scanSuccessful) {
            deleteStales(existingVideoIds);
        }
        return videoRepository.findAll();
    }

    public UserProgress getUserProgress(Long videoId) {
        return userProgressRepository.findByVideo_Id(videoId);
    }

    public boolean fileScanner(File root) {
        boolean scanComplete = true;
        if (root.isDirectory()) {
            File[] allFiles = root.listFiles();
            if (allFiles == null) {
                System.out.println("Could not access directory: " + root.getAbsolutePath());
                return false;
            }
            for (File f : allFiles) {
                if (IGNORE_FOLDERS.contains(f.getName().toLowerCase())) {
                    continue;
                }
                if (f.isDirectory()) {
                    boolean success = fileScanner(f);
                    if (!success) {
                        scanComplete = false;
                    }
                } else {
                    checkFile(f);
                }
            }
        } else {
            checkFile(root);
        }
        return scanComplete;
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
            videoName = videoName.trim();

            if (videoName.startsWith("```")) {
                videoName = videoName.replaceFirst("^```(?:json)?\\s*", "");
                videoName = videoName.replaceFirst("\\s*```$", "");
                videoName = videoName.trim();
            }

            JsonNode jsonNode = mapper.readTree(videoName);

            String name = jsonNode.path("title").asText("");
            String year = jsonNode.path("year").asText("");

            System.out.println("Name: " + name);
            System.out.println("Year: " + year);

            // AI could not identify the movie
            if (name.isBlank() || name.equalsIgnoreCase("not found")) {
                System.out.println("AI could not identify movie: " + videoName);
                return null;
            }

           return tmdbService.getDetails(name, year);
        } catch (Exception e) {
            e.printStackTrace();
            return new MovieMetadata("Default", "Error fetching data", "", "");
        }
    }

    public void saveFile(File videoFile) {
        String videoString = videoFile.getName();
        String videoName = nameParser(videoString);

        // get clean name from ai
        String cleanName = aiService.cleanNameWithAi(videoName);
        System.out.println("AI Cleaned Name" + cleanName);
        MovieMetadata metadata = getTmdbMetadata(cleanName);

        if (metadata == null) {
            metadata = new MovieMetadata("Default", "Error fetching data", "", "");
        }

        Video v = new Video();
        v.setFileName(metadata.title() != null ? metadata.title() : cleanName);
        v.setFilePath(videoFile.getAbsolutePath());
        v.setPosterPath(metadata.posterUrl() != null ? metadata.posterUrl() : null);
        v.setOverView(metadata.overview() != null ? metadata.overview() : null);
        v.setReleaseDate(metadata.releaseDate() != null ? metadata.releaseDate() : null);
        try {
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