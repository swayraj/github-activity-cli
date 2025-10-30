package com.gitcli.github_activity_cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class GithubActivityCliApplication implements CommandLineRunner, ExitCodeGenerator {

    private final GitHubActivityCommand gitHubActivityCommand;
    private int exitCode;

    public GithubActivityCliApplication(GitHubActivityCommand gitHubActivityCommand) {
        this.gitHubActivityCommand = gitHubActivityCommand;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(
                SpringApplication.run(GithubActivityCliApplication.class, args)
        ));
    }

    @Override
    public void run(String... args) throws Exception {
        this.exitCode = new CommandLine(gitHubActivityCommand).execute(args);
    }

    @Override
    public int getExitCode() {
        return this.exitCode;
    }
}