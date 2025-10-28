package com.gitcli.github_activity_cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

        try
        {
            String jsonResponse = gitHubService.fetchUserEvents(username);
            System.out.println("--- RAW JSON RESPONSE ---");
            System.out.println(jsonResponse);
        }
        catch (Exception e)
        {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
