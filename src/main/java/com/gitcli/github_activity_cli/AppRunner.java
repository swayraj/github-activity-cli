package com.gitcli.github_activity_cli;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    @Override
    public void run(String...  args) throws Exception
    {
        System.out.println("Hello from Command Line Runner");

        if (args.length > 0) {
            System.out.println("You passed in this argument: " + args[0]);
        } else {
            System.out.println("No arguments passed.");
        }
    }
}
