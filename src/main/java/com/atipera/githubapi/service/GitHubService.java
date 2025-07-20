package com.atipera.githubapi.service;

import com.atipera.githubapi.dto.BranchDto;
import com.atipera.githubapi.dto.RepositoryDto;
import com.atipera.githubapi.exception.UserNotFoundException;
import com.atipera.githubapi.model.GitHubBranch;
import com.atipera.githubapi.model.GitHubRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GitHubService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public GitHubService(@Value("${github.api.base-url:https://api.github.com}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    public List<RepositoryDto> getUserRepositories(String username) {
        try {
            List<GitHubRepository> repositories = fetchUserRepositories(username);

            return repositories.stream()
                    .filter(repo -> !repo.isFork())
                    .map(repo -> buildRepositoryDto(repo, username))
                    .toList();

        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException("User '" + username + "' not found");
        }
    }

    private List<GitHubRepository> fetchUserRepositories(String username) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<GitHubRepository>> response = restTemplate.exchange(
                baseUrl + "/users/{username}/repos",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<GitHubRepository>>() {},
                username
        );

        return response.getBody();
    }

    private RepositoryDto buildRepositoryDto(GitHubRepository repo, String username) {
        List<BranchDto> branches = fetchRepositoryBranches(username, repo.name());

        return new RepositoryDto(
                repo.name(),
                repo.owner().login(),
                branches
        );
    }

    private List<BranchDto> fetchRepositoryBranches(String username, String repositoryName) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<GitHubBranch>> response = restTemplate.exchange(
                baseUrl + "/repos/{owner}/{repo}/branches",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<GitHubBranch>>() {},
                username,
                repositoryName
        );

        return response.getBody().stream()
                .map(branch -> new BranchDto(branch.name(), branch.commit().sha()))
                .toList();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "Atipera-GitHub-API");
        return headers;
    }
}