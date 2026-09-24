package com.nexus.server.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class AiService {

    private final RestClient restClient;

    public AiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String cleanNameWithAi(String videoName) {

        String apiKey = System.getenv("AI_API_KEY");

        String prompt = """
                You are a movie filename parser.

                Extract the movie title and release year from the provided video filename.

                Rules:
                1. Return ONLY valid JSON with exactly these two keys:
                   "title"
                   "year"

                2. Remove file extensions such as:
                   .mp4, .mkv, .avi, .mov

                3. Ignore technical metadata such as:
                   480p, 720p, 1080p, 2160p, 4K, BluRay, WEB-DL, WEBRip,
                   HDR, HEVC, x264, x265, H264, AAC, DTS, 5.1, 7.1

                4. Ignore release-group names, hashes, bracketed tags, language tags,
                   and other downloading/source metadata.

                5. Preserve the actual movie title. Do not invent or replace it
                   with another movie.

                6. Only return a year when a 4-digit release year is explicitly
                   identifiable from the filename. Otherwise return "not found".

                7. If the filename does not contain enough information to identify
                   a movie with reasonable confidence, return:
                   {"title":"not found","year":"not found"}

                8. Generic filenames such as "movie.mp4", "video.mp4", "mp4.mp4",
                   "sample.mp4", "day1.mp4", etc. should not be treated as movie
                   titles unless there is strong evidence in the filename.

                Examples:

                Filename:
                The.Dark.Knight.2008.1080p.BluRay.x264.mp4

                Output:
                {"title":"The Dark Knight","year":"2008"}

                Filename:
                Jack.Reacher.Never.Go.Back.2016.1080p.WEB-DL.mkv

                Output:
                {"title":"Jack Reacher: Never Go Back","year":"2016"}

                Filename:
                movie.mp4

                Output:
                {"title":"not found","year":"not found"}

                Filename:
                American.Pie.1999.720p.BluRay.mkv

                Output:
                {"title":"American Pie","year":"1999"}

                Now parse this filename:
                """ + videoName;

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode payload = mapper.createObjectNode();

        payload.put("model", "google/gemini-2.5-flash-lite");

        ArrayNode messages = payload.putArray("messages");

        ObjectNode message = messages.addObject();
        message.put("role", "user");
        message.put("content", prompt);

        try {
            Thread.sleep(300);

            JsonNode response = restClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("openrouter.ai")
                            .path("api/v1/chat/completions")
                            .build())
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload.toString())
                    .retrieve()
                    .body(JsonNode.class);

            String aiResult = response
                    .get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();

            return aiResult;

        } catch (Exception e) {
            e.printStackTrace();

            // Keep the same general contract expected by MediaScannerService.
            return """
                    {"title":"not found","year":"not found"}
                    """;
        }
    }
}