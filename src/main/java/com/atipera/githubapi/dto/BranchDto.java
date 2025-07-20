package com.atipera.githubapi.dto;

public record BranchDto(
        String name,
        String lastCommitSha
) {}