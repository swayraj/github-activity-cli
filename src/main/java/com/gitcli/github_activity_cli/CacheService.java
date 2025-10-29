package com.gitcli.github_activity_cli;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class CacheService {

    // We'll store our cache in a 'cache' directory in the project folder
    private final Path cacheDir = Paths.get("cache");

    // How long a cache entry is valid (e.g., 10 minutes)
    private static final long CACHE_DURATION_MINUTES = 10;

    public CacheService() {
        try {
            // Create the 'cache' directory if it doesn't exist
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + e.getMessage());
        }
    }

    /**
     * Tries to get a non-stale cached response for a user.
     */
    public Optional<String> get(String username) {
        Path cacheFile = cacheDir.resolve(username + ".json");

        if (!Files.exists(cacheFile)) {
            return Optional.empty(); // No cache file
        }

        try {
            // Check how old the file is
            Instant lastModified = Files.getLastModifiedTime(cacheFile).toInstant();
            long ageMinutes = ChronoUnit.MINUTES.between(lastModified, Instant.now());

            if (ageMinutes > CACHE_DURATION_MINUTES) {
                System.out.println("Cache is stale. Ignoring.");
                return Optional.empty(); // Cache is too old
            }

            // Cache is valid! Read and return the JSON string.
            System.out.println("Returning fresh response from cache...");
            return Optional.of(Files.readString(cacheFile));

        } catch (IOException e) {
            System.err.println("Failed to read cache file: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Saves a new JSON response to the cache.
     */
    public void set(String username, String jsonResponse) {
        Path cacheFile = cacheDir.resolve(username + ".json");
        try {
            // Write the raw JSON string to the file
            Files.writeString(cacheFile, jsonResponse);
        } catch (IOException e) {
            System.err.println("Failed to write to cache: " + e.getMessage());
        }
    }
}
