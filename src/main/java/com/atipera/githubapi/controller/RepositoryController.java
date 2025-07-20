package com.atipera.githubapi.controller;

import com.atipera.githubapi.dto.ErrorResponseDto;
import com.atipera.githubapi.dto.RepositoryDto;
import com.atipera.githubapi.exception.UserNotFoundException;
import com.atipera.githubapi.service.GitHubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RepositoryController {

    private final GitHubService gitHubService;

    public RepositoryController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/users/{username}/repos")
    public List<RepositoryDto> getUserRepositories(@PathVariable String username) {
        return gitHubService.getUserRepositories(username);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException e) {
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}