package com.gitcli.github_activity_cli;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubRepo(String name) {

}
