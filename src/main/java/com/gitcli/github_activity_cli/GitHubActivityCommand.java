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
            List<GitHubEvent> events = gitHubService.fetchUserEvents(username);

            if (events.isEmpty()) {
                System.out.println("No recent activity found or user does not exist.");
                return 0; // 0 means success
            }


            for (GitHubEvent event : events) {
                System.out.printf(
                        "[%s] %s on %s (ID: %s)%n",
                        event.created_at(),
                        event.type(),
                        event.repo().name(),
                        event.id()
                );
            }

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return 1; // 1 means error
        }

        return 0; // 0 means success
    }
}
