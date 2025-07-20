package com.atipera.githubapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubBranch(
        String name,
        Commit commit
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Commit(String sha) {}
}