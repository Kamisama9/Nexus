package com.nexus.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class AiService {

    private final RestClient restClient;
    
    public AiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String cleanNameWithAi(String videoName) {
        // Call AI API to clean the video name
        String apiKey = System.getenv("AI_API_KEY");
        String prompt = "Extract the movie title and release year from this file name: '" + videoName + "'. " +
                "Return ONLY a valid JSON object with the keys 'title' and 'year'. " +
                "Do not include any other text, markdown formatting, or explanations.";
                
        String payLoad = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{ \"role\": \"user\", \"content\": \"" + prompt + "\" }] }";

            try {
                Thread.sleep(300);

                JsonNode response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host("openrouter.ai")
                    .path("api/v1/chat/completions")
                    .build())
                .header("Authorization", "Bearer " + apiKey)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(payLoad)
                .retrieve() 
                .body(JsonNode.class);

                String aiResult = response.get("choices").get(0).get("message").get("content").asText();
                return aiResult;    

                
            } catch (Exception e) {
                e.printStackTrace();
                return videoName; // Fallback to original name if AI fails
            }
    }
}
