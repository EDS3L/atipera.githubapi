# GitHub Repositories API

Spring Boot application that provides REST API for listing GitHub user repositories with branch information.

## Features

- List all non-fork repositories for a given GitHub user
- For each repository, provide repository name, owner login, and branch details
- Each branch includes name and last commit SHA
- Proper error handling for non-existing users (404 response)
- Integration with GitHub API v3

## Technology Stack

- Java 21
- Spring Boot 3.5.3
- Gradle
- JUnit 5

## API Endpoints

### Get User Repositories
```
GET /api/users/{username}/repos
```

**Response (Success - 200):**
```json
[
  {
    "repositoryName": "EXAMPLE",
    "ownerLogin": "EDS3L",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "7fd1a60b01f91b314f59955a4e4d4e80d8edf11d"
      },
      {
        "name": "develop",
        "lastCommitSha": "6dcb09b5b57875f334f61aebed695e2e4193db5e"
      }
    ]
  }
]
```

**Response (User Not Found - 404):**
```json
{
  "status": 404,
  "message": "User 'eds3l123213' not found"
}
```

## Running the Application

### Prerequisites
- Java 21
- gradke

### Running locally
```bash
mvn spring-boot:run
```

The application will start on port 8081.

### Building
```bash
mvn clean package
```

### Running tests
```bash
mvn test
```

## Configuration

The application can be configured via `application.yml`:

```yaml
github:
  api:
    base-url: https://api.github.com
```

## Example Usage

```bash
# Get repositories for user 'eds3l'
curl http://localhost:8080/api/users/EDS3L/repos

# Test error handling
curl http://localhost:8080/api/users/EDS3L123123/repos
```

