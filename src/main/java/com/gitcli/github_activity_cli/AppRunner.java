package com.gitcli.github_activity_cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AppRunner implements CommandLineRunner {

    private final GitHubService gitHubService;

    public AppRunner(GitHubService gitHubService)
    {
        this.gitHubService = gitHubService;
    }

    @Override
    public void run(String... args) throws Exception
    {
        if(args.length == 0)
        {
            System.err.println("Error: Please provide a GitHub username.");
            System.out.println("Usage: java -jar app.jar <username>");
            return;
        }

        String username = args[0];
        System.out.println("Fetching activity for user: " + username);
        System.out.println("--- RECENT ACTIVITY ---");

        try {
            // 1. This now returns a List of objects, not a String
            List<GitHubEvent> events = gitHubService.fetchUserEvents(username);

            if (events.isEmpty()) {
                System.out.println("No recent activity found or user does not exist.");
                return;
            }

            // 2. Loop through the list and print a clean summary
            //    We use the record's accessor methods: .type() and .repo()
            for (GitHubEvent event : events) {
                System.out.printf(
                        "[%s] - %s%n", // %s is a string, %n is a new line
                        event.type(),
                        event.repo().name()
                );
            }

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
