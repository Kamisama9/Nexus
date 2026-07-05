package com.nexus.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.nexus.server.dto.MovieMetadata;
@Service
public class TmdbService {

    private final RestClient restClient;
    
    public TmdbService(RestClient restClient) {
        this.restClient = restClient;
    }

    public MovieMetadata getDetails(String movieName) {
        // Call TMDB API to get movie details based on the movie name
        String apiKey = System.getenv("TMDB_API_KEY");
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + movieName;

         try {
        Thread.sleep(2000);

        JsonNode response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .scheme("https")
                .host("api.themoviedb.org")
                .path("/3/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", movieName)
                .build())
            .retrieve()
            .body(JsonNode.class);

        JsonNode results = response.get("results");
            if (results != null && results.isArray() && !results.isEmpty()) {
                
                // 2. Grab the very first search result
                JsonNode firstMovie = results.get(0);

                // 3. Extract the exact fields you want
                String title = firstMovie.path("title").asText("");
                String overview = firstMovie.path("overview").asText("");
                String releaseDate = firstMovie.path("release_date").asText("");
                
                // TMDB only gives the end of the poster path, so we attach the base URL
                String posterPath = firstMovie.path("poster_path").asText("");
                String fullPosterUrl = posterPath.isEmpty() ? "" : "https://image.tmdb.org/t/p/w500" + posterPath;

                // 4. Return the neatly packaged data
                return new MovieMetadata(title, overview, fullPosterUrl, releaseDate);
            }else
            {
                System.out.println("No results found for: " + movieName);
            return new MovieMetadata("NONE", "Overview not available", "", "url not available");
            }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return new MovieMetadata(movieName, "Error fetching data", "", "");
    }

    }
