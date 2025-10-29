package com.gitcli.github_activity_cli;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}
