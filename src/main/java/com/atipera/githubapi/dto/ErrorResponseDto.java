package com.atipera.githubapi.dto;

public record ErrorResponseDto(
        int status,
        String message
) {}