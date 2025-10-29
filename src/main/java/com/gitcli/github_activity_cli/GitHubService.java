package com.gitcli.github_activity_cli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Optional; // This import is now used

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class GitHubService {

    private static final String GITHUB_API_URL = "https://api.github.com/users/";

    private final CacheService cacheService;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GitHubService(ObjectMapper objectMapper, CacheService cacheService) {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = objectMapper;
        this.cacheService = cacheService;
    }

    public List<GitHubEvent> fetchUserEvents(String username) throws IOException, InterruptedException {

        // --- 1. TRY TO GET FROM CACHE FIRST ---
        Optional<String> cachedResponse = cacheService.get(username);

        if (cachedResponse.isPresent()) {
            // If the cache is present and fresh, parse it and return.
            // We skip the API call entirely.
            return objectMapper.readValue(cachedResponse.get(), new TypeReference<List<GitHubEvent>>() {});
        }

        // --- 2. IF CACHE MISS, CALL THE API ---
        // (Log line moved here, so it only prints on a cache miss)
        System.out.println(">>> Calling GitHub API for user: " + username);

        String url = GITHUB_API_URL + username + "/events";
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", "github-activity-cli-app")
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // --- ERROR HANDLING (NO CHANGE) ---
        if (response.statusCode() == 404) {
            throw new UserNotFoundException(username);
        }
        if (response.statusCode() != 200) {
            System.err.println("Error: Received non-200 response:" + response.statusCode());
            return List.of();
        }

        String jsonResponse = response.body();

        // --- 3. SAVE THE NEW RESPONSE TO CACHE ---
        cacheService.set(username, jsonResponse);

        // --- 4. PARSE AND RETURN ---
        return objectMapper.readValue(jsonResponse, new TypeReference<List<GitHubEvent>>() {});
    }
}