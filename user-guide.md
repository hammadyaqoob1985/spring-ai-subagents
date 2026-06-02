# user-guide.md

## Purpose
Spring AI Sub Agents is a Spring Boot application that **orchestrates multiple “sub-agents”** using **Spring AI**. You provide agent task definitions as **Markdown files**, and the application runs them against an LLM (e.g., OpenAI) to produce a coordinated result.

## Prerequisites
- Java 21+ (recommended)
- Maven/Gradle build tooling (use the project’s build system)
- An LLM provider account and API key
- An agent task Markdown file available on the classpath (see `agent.tasks.paths`)

## Configuration
Key properties (from `src/main/resources/application.properties`):
- `OPENAI_API_KEY` (env var)
  - Mapped to: `spring.ai.openai.api-key`.
- `spring.ai.openai.chat.model`
  - Default in this project: `gpt-5.4-nano`.
- `agent.tasks.paths`
  - Default: `classpath*:agents/*.md`
  - Controls where the app loads agent markdown task files from.

### Set environment variables
- `OPENAI_API_KEY=...`

You can override any Spring property via environment variables or `application-*.properties` if supported by your setup.

## How to run
From the project directory:
- Start the app using your IDE, or run the build + boot task for your project.

Typical Spring Boot run commands (pick the one that matches your build):
- Maven: `mvn spring-boot:run`
- Gradle: `./gradlew bootRun`

After startup, the application will load markdown task definitions from `agents/*.md` on the classpath.

## Providing agent Markdown tasks
The application loads task files from:
- `agent.tasks.paths=classpath*:agents/*.md`

### Where to place tasks
Create a directory in your resources:
- `src/main/resources/agents/`

Add one or more Markdown files, e.g.:
- `src/main/resources/agents/triage.md`
- `src/main/resources/agents/research.md`

### Markdown format (what the app expects)
- Each Markdown file represents a task definition for one “sub-agent”.
- Include clear instructions, inputs, and any expected output structure.

> If you want the exact schema (front-matter keys, template variables, etc.), inspect the existing agent markdown files under `src/main/resources/agents/` in your repository.

## Example usage (prompt + expected behavior)
### What prompt to send
Send a natural-language request that the orchestrator can map to the sub-agent tasks. For example:

**Request**
> “Plan and draft a short technical design for a Spring Boot service that orchestrates AI sub-agents. Include: goals, components, data flow, and risks.”

### Expected behavior
- The orchestrator selects/executes relevant sub-agent markdown tasks loaded from `agents/*.md`.
- Each sub-agent calls the configured LLM model (`spring.ai.openai.chat.model`).
- The final response is a coordinated output (typically a combined narrative and/or structured sections) derived from the sub-agent results.

## Notes / troubleshooting
- If you get authentication errors, verify `OPENAI_API_KEY` is set.
- If no sub-agent tasks run, confirm that `agent.tasks.paths` matches your actual resource location (and that files are under `src/main/resources/agents/`).
- If the app cannot load tasks, ensure Markdown files are included in the built artifact (resources are packaged).
