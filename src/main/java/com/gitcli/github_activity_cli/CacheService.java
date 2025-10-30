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

    private final Path cacheDir = Paths.get("cache");
    private static final long CACHE_DURATION_MINUTES = 10;

    public CacheService() {
        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            System.err.println("Failed to create cache directory: " + e.getMessage());
        }
    }

    public Optional<String> get(String username) {
        Path cacheFile = cacheDir.resolve(username + ".json");

        if (!Files.exists(cacheFile)) {
            return Optional.empty();
        }

        try {
            Instant lastModified = Files.getLastModifiedTime(cacheFile).toInstant();
            long ageMinutes = ChronoUnit.MINUTES.between(lastModified, Instant.now());

            if (ageMinutes > CACHE_DURATION_MINUTES) {
                // --- LOGGING IS BACK IN THIS FILE ---
                System.out.println("Cache is stale. Ignoring.");
                return Optional.empty();
            }

            // --- LOGGING IS BACK IN THIS FILE ---
            System.out.println("Returning fresh response from cache...");
            return Optional.of(Files.readString(cacheFile));

        } catch (IOException e) {
            System.err.println("Failed to read cache file: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void set(String username, String jsonResponse) {
        Path cacheFile = cacheDir.resolve(username + ".json");
        try {
            Files.writeString(cacheFile, jsonResponse);
        } catch (IOException e) {
            System.err.println("Failed to write to cache: " + e.getMessage());
        }
    }
}