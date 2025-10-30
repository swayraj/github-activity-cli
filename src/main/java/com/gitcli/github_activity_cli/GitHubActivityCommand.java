package com.gitcli.github_activity_cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import java.util.List;
import java.util.concurrent.Callable;

@Component
@Command(
        name = "github-activity",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Fetches recent public activity for a GitHub user."
)
public class GitHubActivityCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The GitHub username to query.")
    private String username;

    @Option(
            names = {"-t", "--type"},
            description = "Filter events by type (e.g., PushEvent, CreateEvent)."
    )
    private String eventType;

    private final GitHubService gitHubService;

    // Constructor is back to just GitHubService
    public GitHubActivityCommand(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Fetching activity for user: " + username);

        try {
            List<GitHubEvent> allEvents = gitHubService.fetchUserEvents(username);

            if (allEvents.isEmpty()) {
                System.out.println("No recent activity found or user does not exist.");
                return 0;
            }

            var eventStream = allEvents.stream();

            if (eventType != null && !eventType.isBlank()) {
                System.out.println("Filtering by type: " + eventType);
                eventStream = eventStream.filter(event ->
                        event.type().equalsIgnoreCase(eventType)
                );
            }

            List<GitHubEvent> filteredEvents = eventStream.toList();

            if (filteredEvents.isEmpty()) {
                System.out.println("No activity found for the specified filter.");
                return 0;
            }

            // Standard, non-color printf
            String formatString = "[%s] %s by %s on %s (ID: %s)%n";

            for (GitHubEvent event : filteredEvents) {
                System.out.printf(
                        formatString,
                        event.created_at(),
                        event.type(),
                        event.actor().login(),
                        event.repo().name(),
                        event.id()
                );
            }

        } catch (UserNotFoundException e) {
            // Standard, non-color error print
            System.err.println("Error: The GitHub user '" + username + "' was not found.");
            return 1;

        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }

        return 0;
    }
}