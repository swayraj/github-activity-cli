package com.gitcli.github_activity_cli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
public class GitHubService{

    private static final String GITHUB_API_URL = "https://api.github.com/users/";

    private HttpClient httpClient;

    private final ObjectMapper objectMapper;

    public GitHubService(ObjectMapper objectMapper)
    {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.objectMapper = objectMapper;
    }

    public List<GitHubEvent> fetchUserEvents(String username) throws IOException, InterruptedException
    {
        String url = GITHUB_API_URL+ username + "/events";
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("User-Agent", "github-activity-cli-app")
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200)
        {
            System.err.println("Error: Received non-200 response:" + response.statusCode());

            return List.of();
        }

        String jsonResponse = response.body();

        return objectMapper.readValue(jsonResponse, new TypeReference<List<GitHubEvent>>() {});
    }
}
