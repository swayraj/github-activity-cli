package com.gitcli.github_activity_cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.concurrent.Callable;
import java.util.List;


@Component
@CommandLine.Command(
        name="github-activity",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Fetches recent public activity for a GitHub user."
)
public class GitHubActivityCommand implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "Username to query")
    private String username;

    private final GitHubService gitHubService;

    // This creates an optional flag '--type' (or '-t')
    @CommandLine.Option(
            names = {"-t", "--type"},
            description = "Filter events by type (e.g., PushEvent, CreateEvent)."
    )
    private String eventType;

    public GitHubActivityCommand(GitHubService gitHubService)
    {
        this.gitHubService = gitHubService;
    }

    // This 'call()' method is run by Picocli
    @Override
    public Integer call() throws Exception {
        System.out.println("Fetching activity for user: " + username);
        System.out.println("--- RECENT ACTIVITY ---");

        try {
            // 1. Get ALL events from the service, just like before
            List<GitHubEvent> allEvents = gitHubService.fetchUserEvents(username);

            if (allEvents.isEmpty()) {
                System.out.println("No recent activity found or user does not exist.");
                return 0;
            }

            // 2. --- THIS IS THE NEW FILTER LOGIC ---
            // Start with a stream of all events
            var eventStream = allEvents.stream();

            // If the user *provided* an eventType (it's not null)
            if (eventType != null && !eventType.isBlank()) {
                System.out.println("Filtering by type: " + eventType);
                // Re-assign the stream to a new, filtered stream
                eventStream = eventStream.filter(event ->
                        event.type().equalsIgnoreCase(eventType)
                );
            }

            // 3. Collect the final stream (either filtered or not) into a list
            List<GitHubEvent> filteredEvents = eventStream.toList();

            // 4. Check if the *filtered* list is empty
            if (filteredEvents.isEmpty()) {
                System.out.println("No activity found for the specified filter.");
                return 0;
            }

            // 5. Loop over the FINAL filtered list
            for (GitHubEvent event : filteredEvents) {
                System.out.printf(
                        "[%s] %s by %s on %s (ID: %s)%n",
                        event.created_at(),
                        event.type(),
                        event.actor().login(),
                        event.repo().name(),
                        event.id()
                );
            }

        } catch (Exception e) { // <-- We'll improve this next
            System.err.println("An error occurred: " + e.getMessage());
            return 1;
        }

        return 0; // 0 means success
    }
}
