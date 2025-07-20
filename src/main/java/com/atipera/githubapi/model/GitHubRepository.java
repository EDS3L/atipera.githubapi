package com.atipera.githubapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubRepository(
        String name,
        @JsonProperty("fork") boolean isFork,
        Owner owner
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Owner(String login) {}
}