package com.atipera.githubapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnRepositoriesForExistingUserAndHandleNonExistingUser() throws Exception {
        String existingUsername = "EDS3L";

        // Existing user
        mockMvc.perform(get("/api/users/{username}/repos", existingUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$[0].repositoryName", is(notNullValue())))
                .andExpect(jsonPath("$[0].ownerLogin", is(existingUsername)))
                .andExpect(jsonPath("$[0].branches", isA(java.util.List.class)))
                .andExpect(jsonPath("$[0].branches[0].name", is(notNullValue())))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha", is(notNullValue())))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha", matchesPattern("^[a-f0-9]{40}$")));

        String nonExistingUsername = "EDS3L71239320";

        // Non-existing user
        mockMvc.perform(get("/api/users/{username}/repos", nonExistingUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", containsString("not found")))
                .andExpect(jsonPath("$.message", containsString(nonExistingUsername)));
    }
}
