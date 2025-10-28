package com.gitcli.github_activity_cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class GithubActivityCliApplication implements CommandLineRunner {

    private final GitHubActivityCommand gitHubActivityCommand;

    public GithubActivityCliApplication(GitHubActivityCommand gitHubActivityCommand) {
        this.gitHubActivityCommand = gitHubActivityCommand;
    }

    // Standard main method
    public static void main(String[] args) {
        SpringApplication.run(GithubActivityCliApplication.class, args);
    }

    // This 'run' method is our new entry point
    @Override
    public void run(String... args) throws Exception {
        // This launches Picocli, which will parse the 'args'
        // and execute our GitHubActivityCommand
        int exitCode = new CommandLine(gitHubActivityCommand).execute(args);

        // This makes sure our app exits with the code from our command
        System.exit(exitCode);
    }
}
